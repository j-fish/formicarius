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

package fr.com.jfish.jfgformicarius.formicarius.helpers.entities.maincharacter;

import fr.com.jfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jfish.jfgformicarius.formicarius.entities.pools.CaveZoneInteractableEntityPool;
import fr.com.jfish.jfgformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.TransitionAction;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.ZoneBuilder;
import fr.com.jfish.jfgformicarius.formicarius.world.zone.ZonePosition;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class MainCharacterCaveZoneHelper implements TransitionAction {
       
    /**
     * MainCharacter instance.
     */
    private final MainCharacter mainCharacter;

    /**
     * Game instance.
     */
    private final Game game;
    
    /**
     * ZoneBuilder instance.
     */
    private ZoneBuilder zoneBuilder;
    
    /**
     * 
     */
    final Map<ZonePosition, ZoneBuilder> zones;

    /**
     * 
     * @param mainCharacter
     * @param game 
     * @param zones 
     */
    public MainCharacterCaveZoneHelper(final MainCharacter mainCharacter, final Game game, 
            final HashMap<ZonePosition, ZoneBuilder> zones) {
        this.mainCharacter = mainCharacter;
        this.game = game;
        this.zones = zones;
        mainCharacter.setCurrentZonePosition(new ZonePosition(0, 0));
    }
    
    @Override
    public void triggerTransition(final int direction) {
        
         // Clear Spawnable :
        if (mainCharacter.getSpawnable() != null && mainCharacter.getSpawnable().isSpawned()) {
            mainCharacter.getSpawnable().clear();
        }
        // Clear Ingnitable :
        if (mainCharacter.getIgnitable() != null && mainCharacter.getIgnitable().isIgnited()) {
            mainCharacter.getIgnitable().clear();
        }

        // Clear entity helperscollections : objects, iteractable + all entities
        // other than main character and cat :
        game.clearEntityCollectionsForTransition();
        
        try {
            zoneBuilder = zones.get(mainCharacter.getCurrentZonePosition());
            zoneBuilder.buildZone(null);
            game.getEntityHelper().getObjectEntities().putAll(zoneBuilder.getGlobals());
        } catch (final ZoneBuildException ex) {
            Logger.getLogger(MainCharacterCaveZoneHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Fill the following HashMap with randomly accessed pool of Interactable
        // class instances. /!\ before trim entities for collisions :
        // remove entities in collision.
        game.getEntityHelper().getInteractableEntities().putAll(
            CaveZoneInteractableEntityPool.getInstance().getRandomSubPool());
    }
    
}
