/* **************************************************************************************
 * Copyright (c) 2018 Calypso Networks Association https://www.calypsonet-asso.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 ************************************************************************************** */
package org.eclipse.keyple.example.generic.standalone.UseCase3_SequentialMultiSelection;

import org.eclipse.keyple.core.card.selection.AbstractSmartCard;
import org.eclipse.keyple.core.card.selection.CardSelectionsResult;
import org.eclipse.keyple.core.card.selection.CardSelectionsService;
import org.eclipse.keyple.core.card.selection.CardSelector;
import org.eclipse.keyple.core.service.Plugin;
import org.eclipse.keyple.core.service.Reader;
import org.eclipse.keyple.core.service.SmartCardService;
import org.eclipse.keyple.core.util.ByteArrayUtil;
import org.eclipse.keyple.example.generic.standalone.common.GenericCardSelectionRequest;
import org.eclipse.keyple.example.generic.standalone.common.PcscReaderUtils;
import org.eclipse.keyple.plugin.pcsc.PcscPluginFactory;
import org.eclipse.keyple.plugin.pcsc.PcscReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The UseCase_Generic3_GroupedMultiSelection_Pcsc class illustrates the use of the select next
 * mechanism
 */
public class Main_SequentialMultiSelection_Pcsc {
  private static final Logger logger =
      LoggerFactory.getLogger(Main_SequentialMultiSelection_Pcsc.class);

  public static void main(String[] args) {

    // Get the instance of the SmartCardService (Singleton pattern)
    SmartCardService smartCardService = SmartCardService.getInstance();

    // Register the PcscPlugin with SmartCardService, get the corresponding generic Plugin in return
    // This example does not use observation, no exception handler is defined.
    Plugin plugin = smartCardService.registerPlugin(new PcscPluginFactory(null, null));

    // Get and configure the PO reader
    Reader reader = plugin.getReader(PcscReaderUtils.getContactlessReaderName());
    ((PcscReader) reader).setContactless(true);
    ((PcscReader) reader).setIsoProtocol(PcscReader.IsoProtocol.T1);

    logger.info(
        "=============== UseCase Generic #4: AID based sequential explicit multiple selection "
            + "==================");
    logger.info("= Card reader  NAME = {}", reader.getName());

    // Check if a card is present in the reader
    if (!reader.isCardPresent()) {
      logger.error("No card was detected.");
    }

    CardSelectionsService cardSelectionsService;

    // operate card AID selection (change the AID prefix here to adapt it to the card used for
    // the test [the card should have at least two applications matching the AID prefix])
    String cardAidPrefix = "315449432E494341";

    // First selection case
    cardSelectionsService = new CardSelectionsService();

    // AID based selection: get the first application occurrence matching the AID, keep the
    // physical channel open
    cardSelectionsService.prepareSelection(
        new GenericCardSelectionRequest(
            CardSelector.builder()
                .aidSelector(
                    CardSelector.AidSelector.builder()
                        .aidToSelect(cardAidPrefix)
                        .fileOccurrence(CardSelector.AidSelector.FileOccurrence.FIRST)
                        .fileControlInformation(CardSelector.AidSelector.FileControlInformation.FCI)
                        .build())
                .build()));

    // Do the selection and display the result
    doAndAnalyseSelection(reader, cardSelectionsService, 1);

    // New selection: get the next application occurrence matching the same AID, close the
    // physical channel after
    cardSelectionsService = new CardSelectionsService();

    cardSelectionsService.prepareSelection(
        new GenericCardSelectionRequest(
            CardSelector.builder()
                .aidSelector(
                    CardSelector.AidSelector.builder()
                        .aidToSelect(cardAidPrefix)
                        .fileOccurrence(CardSelector.AidSelector.FileOccurrence.NEXT)
                        .fileControlInformation(CardSelector.AidSelector.FileControlInformation.FCI)
                        .build())
                .build()));

    // close the channel after the selection
    cardSelectionsService.prepareReleaseChannel();

    // Do the selection and display the result
    doAndAnalyseSelection(reader, cardSelectionsService, 2);

    System.exit(0);
  }

  private static void doAndAnalyseSelection(
      Reader reader, CardSelectionsService cardSelectionsService, int index) {
    CardSelectionsResult cardSelectionsResult =
        cardSelectionsService.processExplicitSelections(reader);
    if (cardSelectionsResult.hasActiveSelection()) {
      AbstractSmartCard smartCard = cardSelectionsResult.getActiveSmartCard();
      logger.info("The card matched the selection {}.", index);
      String atr = smartCard.hasAtr() ? ByteArrayUtil.toHex(smartCard.getAtrBytes()) : "no ATR";
      String fci = smartCard.hasFci() ? ByteArrayUtil.toHex(smartCard.getFciBytes()) : "no FCI";
      logger.info("Selection status for case {}: \n\t\tATR: {}\n\t\tFCI: {}", index, atr, fci);
    } else {
      logger.info("The selection did not match for case {}.", index);
    }
  }
}
