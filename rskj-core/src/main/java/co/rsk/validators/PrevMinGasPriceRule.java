/*
 * This file is part of RskJ
 * Copyright (C) 2017 RSK Labs Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package co.rsk.validators;

import co.rsk.core.Coin;
import co.rsk.mine.BlockGasPriceRange;
import co.rsk.panic.PanicProcessor;
import org.ethereum.core.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by mario on 26/12/16.
 */
public class PrevMinGasPriceRule implements BlockParentDependantValidationRule {

    private static final Logger logger = LoggerFactory.getLogger("blockvalidator");
    private static final PanicProcessor panicProcessor = new PanicProcessor();


    @Override
    public boolean isValid(Block block, Block parent) {
        if(block.isGenesis()) {
            return true;
        }

        if(block.getMinGasPriceAsInteger() == null || parent == null) {
            logger.warn("PrevMinGasPriceRule - blockmingasprice or parent are null");
            return false;
        }

        BlockGasPriceRange range = new BlockGasPriceRange(new Coin(parent.getMinGasPriceAsInteger()));
        boolean result = range.inRange(new Coin(block.getMinGasPriceAsInteger()));
        if(!result) {
            logger.warn("Error validating Min Gas Price.");
            panicProcessor.panic("invalidmingasprice", "Error validating Min Gas Price.");
        }

        return range.inRange(new Coin(block.getMinGasPriceAsInteger()));
    }
}
