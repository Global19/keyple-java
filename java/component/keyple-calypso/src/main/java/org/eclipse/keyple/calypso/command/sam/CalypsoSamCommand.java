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
package org.eclipse.keyple.calypso.command.sam;

import org.eclipse.keyple.calypso.command.sam.builder.security.*;
import org.eclipse.keyple.calypso.command.sam.builder.security.SelectDiversifierCmdBuild;
import org.eclipse.keyple.calypso.command.sam.parser.security.*;
import org.eclipse.keyple.calypso.command.sam.parser.security.SelectDiversifierRespPars;
import org.eclipse.keyple.core.command.SeCommand;

public enum CalypsoSamCommand implements SeCommand {

    /** The sam select diversifier. */
    SELECT_DIVERSIFIER("Select Diversifier", (byte) 0x14, SelectDiversifierCmdBuild.class,
            SelectDiversifierRespPars.class),

    /** The sam get challenge. */
    GET_CHALLENGE("Get Challenge", (byte) 0x84, SamGetChallengeCmdBuild.class,
            SamGetChallengeRespPars.class),

    /** The sam digest init. */
    DIGEST_INIT("Digest Init", (byte) 0x8A, DigestInitCmdBuild.class, DigestInitRespPars.class),

    /** The sam digest update. */
    DIGEST_UPDATE("Digest Update", (byte) 0x8C, DigestUpdateCmdBuild.class,
            DigestUpdateRespPars.class),

    /** The sam digest update multiple. */
    DIGEST_UPDATE_MULTIPLE("Digest Update Multiple", (byte) 0x8C,
            DigestUpdateMultipleCmdBuild.class, DigestUpdateMultipleRespPars.class),

    /** The sam digest close. */
    DIGEST_CLOSE("Digest Close", (byte) 0x8E, DigestCloseCmdBuild.class, DigestCloseRespPars.class),

    /** The sam digest authenticate. */
    DIGEST_AUTHENTICATE("Digest Authenticate", (byte) 0x82, DigestAuthenticateCmdBuild.class,
            DigestAuthenticateRespPars.class),

    /** The sam digest authenticate. */
    GIVE_RANDOM("Give Random", (byte) 0x86, GiveRandomCmdBuild.class, GiveRandomRespPars.class),

    /** The sam digest authenticate. */
    CARD_GENERATE_KEY("Card Generate Key", (byte) 0x12, CardGenerateKeyCmdBuild.class,
            CardGenerateKeyRespPars.class),

    /** The sam unlock. */
    UNLOCK("Unlock", (byte) 0x20, UnlockCmdBuild.class, UnlockRespPars.class),

    /** The sam write key command. */
    WRITE_KEY("Write Key", (byte) 0x1A, SamWriteKeyCmdBuild.class, SamWriteKeyRespPars.class),

    READ_KEY_PARAMETERS("Read Key Parameters", (byte) 0xBC, SamReadKeyParametersCmdBuild.class,
            SamReadKeyParametersRespPars.class),

    READ_EVENT_COUNTER("Read Event Counter", (byte) 0xBE, SamReadEventCounterCmdBuild.class,
            SamReadEventCounterRespPars.class),

    READ_CEILINGS("Read Ceilings", (byte) 0xBE, SamReadCeilingsCmdBuild.class,
            SamReadCeilingsRespPars.class);

    /** The name. */
    private final String name;

    /** The instruction byte. */
    private final byte instructionByte;

    /** The command builder class. */
    private final Class<? extends AbstractSamCommandBuilder> commandBuilderClass;

    /** The response parser class. */
    private final Class<? extends AbstractSamResponseParser> responseParserClass;

    /**
     * The generic constructor of CalypsoCommands.
     *
     * @param name the name
     * @param instructionByte the instruction byte
     * @param commandBuilderClass the command builder class
     * @param responseParserClass the response parser class
     */
    CalypsoSamCommand(String name, byte instructionByte,
            Class<? extends AbstractSamCommandBuilder> commandBuilderClass,
            Class<? extends AbstractSamResponseParser> responseParserClass) {
        this.name = name;
        this.instructionByte = instructionByte;
        this.commandBuilderClass = commandBuilderClass;
        this.responseParserClass = responseParserClass;
    }

    /**
     * Gets the name.
     *
     * @return the command name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the instruction byte.
     *
     * @return the value of INS byte
     */
    public byte getInstructionByte() {
        return instructionByte;
    }

    /**
     * Gets the command builder class.
     *
     * @return the corresponding command builder class
     */
    public Class<? extends AbstractSamCommandBuilder> getCommandBuilderClass() {
        return commandBuilderClass;
    }

    /**
     * Gets the response parser class.
     *
     * @return the corresponding response parser class
     */
    public Class<? extends AbstractSamResponseParser> getResponseParserClass() {
        return responseParserClass;
    }
}
