/********************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/
package org.eclipse.keyple.example.remote.wspolling;

import org.eclipse.keyple.example.remote.calypso.DemoThreads;
import org.eclipse.keyple.example.remote.transport.TransportFactory;
import org.eclipse.keyple.example.remote.wspolling.client_retrofit.WsPollingRetrofitFactory;

public class DemoWsPRetrofitMasterServer {


    // blocking : works
    // non blocking : todo

    public static void main(String[] args) throws Exception {

        Boolean isTransmitSync = true; // use Transmit API Blocking or Not Blocking
        Boolean isMasterServer = true; // DemoMaster is the server (and DemoSlave the Client)

        TransportFactory factory = new WsPollingRetrofitFactory(); // HTTP Web Polling


        /**
         * DemoThreads
         */

        DemoThreads.startServer(isTransmitSync, isMasterServer, factory);
        Thread.sleep(1000);
        DemoThreads.startClient(isTransmitSync, !isMasterServer, factory);
    }
}