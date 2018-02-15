package com.thingworx.extensions.dexpi;



import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.communications.common.SecurityClaims;


import java.io.FileInputStream;
import java.util.Properties;

public class DexpiGraphicClient {

    private ConnectedThingClient client;
    private int scanRate;
    private DexpiGraphicBuilderThing dexpiThing;

    public static void main(String[] args) throws Exception {
        DexpiGraphicClient iotKitClient = new DexpiGraphicClient();
        iotKitClient.init();
        iotKitClient.start();
    }

    public void init() throws Exception {
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        Properties props = new Properties();
        props.load(new FileInputStream("config.properties"));
        String twxUrl = props.getProperty("url");
        String appKey = props.getProperty("appKey");
        scanRate = Integer.parseInt(props.getProperty("scanRate"));
        String identifier = props.getProperty("emsId");

        // Set the required configuration information
        ClientConfigurator config = new ClientConfigurator();
        // The uri for connecting to Thingworx
        config.setUri(twxUrl);
        // Reconnect every 3 seconds if a disconnect occurs or if initial connection cannot be made
        config.setReconnectInterval(3);


        SecurityClaims claims = SecurityClaims.fromAppKey(appKey);
        config.setSecurityClaims(claims);

        // Set the name of the client
        config.setName("IotKitRpi");
        // This client is a SDK
        config.setAsSDKType();
        config.ignoreSSLErrors(true);

        client = new ConnectedThingClient(config);

        dexpiThing = new DexpiGraphicBuilderThing(identifier, "IotKit", identifier, client);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    DexpiGraphicClient.this.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void start() throws Exception {
        try {
            // Start the client
            client.start();
            client.bindThing(dexpiThing);

        } catch (Exception eStart) {
          //  LOGGER.error("Failed to start client", eStart);
        }

        // As long as the client has not been shutdown, continue
        while (!client.isShutdown()) {
            // Only process the Virtual Things if the client is connected
            if (client.isConnected()) {
                // Loop over all the Virtual Things and process them
                for (VirtualThing thing : client.getThings().values()) {
                    try {
                        thing.processScanRequest();
                    } catch (Exception e) {
                      //  LOGGER.error("Failed to execute process scan request for thing " + thing.getName(), e);
                    }
                }
            }
            Thread.sleep(scanRate);
        }
    }

    public void stop() throws Exception {
        dexpiThing.shutdown();
        client.shutdown();
    }

}