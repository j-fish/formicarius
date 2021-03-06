/**
 * *****************************************************************************
 * Copyright (c) 2014 - 2015, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *****************************************************************************
 */
package fr.com.jfish.jfgformicarius.formicarius.world.zone;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class ZoneWallCardinalityDefintions {

    /**
     * False wall is down, true wall is up. For example NORTH = true meaning
     * that north wall has an auto generated entrance door/opening to a zone at
     * it's north.
     */
    public boolean north;
    public boolean east;
    public boolean south;
    public boolean west;

    /**
     * constructor.
     *
     * @param c CardinalityDefinition collection
     */
    public ZoneWallCardinalityDefintions(final CardinalityDefinition ... c) {

        boolean n = false;
        boolean nSet = false;
        boolean e = false;
        boolean eSet = false;
        boolean s = false;
        boolean sSet = false;
        boolean w = false;
        boolean wSet = false;

        for (CardinalityDefinition cc : c) {

            if (cc.equals(CardinalityDefinition.NORTH) && !n && !nSet) {
                n = true;
                nSet = true;
            }

            if (cc.equals(CardinalityDefinition.EAST) && !e && !eSet) {
                e = true;
                eSet = true;
            }

            if (cc.equals(CardinalityDefinition.SOUTH) && !s && !sSet) {
                s = true;
                sSet = true;
            }

            if (cc.equals(CardinalityDefinition.WEST) && !w && !wSet) {
                w = true;
                wSet = true;
            }
        }

        this.north = n;
        this.east = e;
        this.south = s;
        this.west = w;
    }

    /**
     * Constructor.
     */
    public ZoneWallCardinalityDefintions() {
        this.north = false;
        this.east = false;
        this.south = false;
        this.west = false;
    }
    
    /**
     * 
     * @return 
     */
    public List<CardinalityDefinition> getCardinalityDefinitions() {
        
        final List<CardinalityDefinition> c = new ArrayList<>();
        
        if (this.north) c.add(CardinalityDefinition.NORTH);
        if (this.east) c.add(CardinalityDefinition.EAST);
        if (this.south) c.add(CardinalityDefinition.SOUTH);
        if (this.west) c.add(CardinalityDefinition.WEST);
        
        return c;
    }

}
