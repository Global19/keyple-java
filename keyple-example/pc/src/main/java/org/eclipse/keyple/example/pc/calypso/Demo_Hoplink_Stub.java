/*
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License version 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html
 */

package org.eclipse.keyple.example.pc.calypso;

import java.io.IOException;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import org.eclipse.keyple.example.common.Demo_HoplinkTransactionEngine;
import org.eclipse.keyple.example.pc.calypso.stub.se.CsmStubSe;
import org.eclipse.keyple.example.pc.calypso.stub.se.HoplinkStubSe;
import org.eclipse.keyple.plugin.pcsc.PcscProtocolSetting;
import org.eclipse.keyple.plugin.stub.StubPlugin;
import org.eclipse.keyple.plugin.stub.StubReader;
import org.eclipse.keyple.plugin.stub.StubSecureElement;
import org.eclipse.keyple.seproxy.*;
import org.eclipse.keyple.seproxy.event.ObservableReader;
import org.eclipse.keyple.seproxy.protocol.SeProtocolSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Demo_Hoplink_Stub {
    private ProxyReader poReader, csmReader;

    /**
     * This object is used to freeze the main thread while card operations are handle through the
     * observers callbacks. A call to the notify() method would end the program (not demonstrated
     * here).
     */
    private static final Object waitForEnd = new Object();

    /**
     * main program entry
     *
     * @param args the program arguments
     * @throws IOException setParameter exception
     * @throws InterruptedException thread exception
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(Demo_Hoplink_Stub.class);

        /* Get the instance of the SeProxyService (Singleton pattern) */
        SeProxyService seProxyService = SeProxyService.getInstance();

        SortedSet<ReaderPlugin> pluginsSet = new ConcurrentSkipListSet<ReaderPlugin>();

        /* Get the instance of the PcscPlugin (Singleton pattern) */
        pluginsSet.add(StubPlugin.getInstance());

        /* Assign StubPlugin to the SeProxyService */
        seProxyService.setPlugins(pluginsSet);

        /* Setting up the transaction engine (implements Observer) */
        Demo_HoplinkTransactionEngine transactionEngine = new Demo_HoplinkTransactionEngine();

        /*
         * Get PO and CSM stub readers.
         */
        StubReader poReader = StubPlugin.getInstance().plugStubReader("poReader");
        StubReader csmReader = StubPlugin.getInstance().plugStubReader("csmReader");

        /* Both readers are expected not null */
        if (poReader == csmReader || poReader == null || csmReader == null) {
            throw new IllegalStateException("Bad PO/CSM setup");
        }

        logger.info("PO Reader  NAME = {}", poReader.getName());
        logger.info("CSM Reader  NAME = {}", csmReader.getName());

        /* Set the PO reader protocol flag */
        poReader.addSeProtocolSetting(
                new SeProtocolSetting(PcscProtocolSetting.SETTING_PROTOCOL_ISO14443_4));

        /* Assign readers to the Hoplink transaction engine */
        transactionEngine.setReaders(poReader, csmReader);

        /* Create 'virtual' Hoplink and CSM SE */
        StubSecureElement hoplinkSE = new HoplinkStubSe();
        StubSecureElement csmSE = new CsmStubSe();

        /* Insert the CSM into the CSM reader */
        csmReader.insertSe(csmSE);

        /* check if the expected CSM is available. */
        if (!transactionEngine.checkCsm()) {
            throw new IllegalStateException("No CSM available! Exit program.");
        }

        /* Set the transactionEngine as Observer of the PO reader */
        ((ObservableReader) poReader).addObserver(transactionEngine);

        /*
         * Insert the Hoplink PO. The transaction engine will do all the scheduled operations as a
         * result of the insert notification.
         */
        poReader.insertSe(hoplinkSE);

        /* Remove the Hoplink PO */
        poReader.removeSe();

        logger.info("END.");
    }
}
