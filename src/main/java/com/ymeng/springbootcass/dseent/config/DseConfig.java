package com.ymeng.springbootcass.dseent.config;

import com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions;
import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.dse.DseCluster;
import com.datastax.driver.dse.DseSession;
import com.datastax.driver.dse.auth.DsePlainTextAuthProvider;
import com.datastax.driver.mapping.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


@Configuration
public class DseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DseCluster dseCluster(
        @Value("${cassandra.host}") String host,
        @Value("${cassandra.cluster.name}") String clusterName,
        @Value("${cassandra.port:9042}") int port ) {

        DseCluster.Builder dseClusterBuilder =
            DseCluster.builder()
                .addContactPoints(host)
                .withPort(port)
                .withClusterName(clusterName);

        // SSL setup
        boolean useSSL = Boolean.valueOf(env.getProperty("cassandra.usessl"));

        if (useSSL) {
            String sslTrustStoreFile = env.getProperty("cassandra.ssl.truststore.file");
            String sslTrustStorePwd = env.getProperty("cassandra.ssl.truststore.passwd");

            SSLContext sslContext = null;

            if (!sslTrustStoreFile.isEmpty()) {
                InputStream is = null;

                try {
                    is = FileUtils.openInputStream(new File(sslTrustStoreFile));

                    // Also works if "mytruststore" is under "resources" package
                    //DseConfig.class.getClassLoader().getResourceAsStream("mytruststore");

                    char[] pwd = sslTrustStorePwd.toCharArray();

                    KeyStore ks = KeyStore.getInstance("JKS");
                    ks.load(is, pwd);

                    // Default algorithm. Can be changed for specific algorithms
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
                    //TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    tmf.init(ks);

                    TrustManager[] tm = tmf.getTrustManagers();

                    sslContext = SSLContext.getInstance("TLS");
                    sslContext.init(null, tm, null);
                } catch (KeyStoreException kse) {
                    kse.printStackTrace();
                } catch (NoSuchAlgorithmException nsae) {
                    nsae.printStackTrace();
                } catch (CertificateException ce) {
                    ce.printStackTrace();
                } catch (KeyManagementException kme) {
                    kme.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            }

            RemoteEndpointAwareJdkSSLOptions sslOptions =
                (RemoteEndpointAwareJdkSSLOptions) RemoteEndpointAwareJdkSSLOptions.builder()
                    .withSSLContext(sslContext)
                    .build();

            dseClusterBuilder.withSSL(sslOptions);
        }

        // User authentication setup
        boolean userAuth = Boolean.valueOf(env.getProperty("cassandra.userauth"));

        if (userAuth) {
            String userName = env.getProperty("cassandra.auth.username");
            String passWord = env.getProperty("cassandra.auth.passwd");

            dseClusterBuilder
                .withAuthProvider(new DsePlainTextAuthProvider(userName, passWord));
        }

        // Reconnection Policy
        dseClusterBuilder.withReconnectionPolicy(new ConstantReconnectionPolicy(5));

        // Retry Policy
        dseClusterBuilder.withRetryPolicy(FallthroughRetryPolicy.INSTANCE);

        // Speculative Execution Policy
        dseClusterBuilder.withSpeculativeExecutionPolicy(new ConstantSpeculativeExecutionPolicy(1, 5));

        return dseClusterBuilder.build();
    }


    @Bean
    public DseSession dseSession(DseCluster dseCluster,
                                 @Value("${cassandra.keyspace}") String keyspace) {

        return dseCluster.connect(keyspace);
    }


    @Bean
    public MappingManager mappingManager(DseSession dseSession) {

        return new MappingManager(dseSession);
    }
}
