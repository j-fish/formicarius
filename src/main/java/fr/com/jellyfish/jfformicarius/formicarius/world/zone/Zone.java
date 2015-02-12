/**
 * *****************************************************************************
 * Copyright (c) 2014, Thomas.H Warner. All rights reserved.
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
package fr.com.jellyfish.jfformicarius.formicarius.world.zone;

import fr.com.jellyfish.jfformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.Background;
import fr.com.jellyfish.jfformicarius.formicarius.entities.tiles.vegetation.Tree;
import fr.com.jellyfish.jfformicarius.formicarius.exceptions.ZoneBuildException;
import fr.com.jellyfish.jfformicarius.formicarius.game.Game;
import fr.com.jellyfish.jfformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jellyfish.jfformicarius.formicarius.utils.CollisionUtils;
import fr.com.jellyfish.jfformicarius.formicarius.utils.SpriteUtils;
import fr.com.jellyfish.jfformicarius.formicarius.utils.ZoneGenerationUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author thw
 */
public class Zone {

    public final Map<String, AbstractEntity> interactables;
    public final Map<String, AbstractEntity> globals;
    public final Map<String, AbstractEntity> statics;
    public final List<Background> backgroundTiles = new ArrayList<>();
    private final Map<String, int[]> randomDefinitions;

    /**
     * Constructor.
     *
     * @param randomDefinitions Definition of randomness values. For key
     * 'Trees', value int[0] will define maximum random value and value int[1]
     * will define expected random number that will validate an action or
     * process to trigger depending on random number returned and it's equality
     * with value int[1] expected integer. Hash map key may be Class name for
     * reflection use.
     * @param entity
     * @throws formicarius.exceptions.ZoneBuildException
     */
    public Zone(final HashMap<String, int[]> randomDefinitions, final AbstractEntity ... entity) 
        throws ZoneBuildException {

        interactables = new HashMap<>();
        globals = new ConcurrentHashMap<>();
        statics = new ConcurrentHashMap<>();
        this.randomDefinitions = new HashMap<>();
        this.randomDefinitions.putAll(randomDefinitions);
        buildZone(entity);
    }

    /**
     * Initialize all gloabal AbstractEntity terrain elements and append to
     * globals HashMap.
     *
     * @throws ZoneBuildException
     * @param entity AbstractEntity array for triming of any colliding static 
     * CollidableObject that obstruct the main, global or any other AbstractEntity
     * sent in param. See end of this method for triming.
     * @throws ZoneBuildException
     */
    private void buildZone(final AbstractEntity[] entities) throws ZoneBuildException {
        
        Iterator iterator = null;
        int index = -1;
        ////////////////////////////////////////////////////////////////////////
        // Build trees :
        for (AbstractEntity ent : ZoneGenerationUtils.buildRandomTerrainBareTrees(
                FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600,
                randomDefinitions.get(Tree.class.getSimpleName().toString())[0],
                randomDefinitions.get(Tree.class.getSimpleName().toString())[1])) {
            this.globals.put(String.valueOf(++index), ent);
        }
        index = -1;
        ////////////////////////////////////////////////////////////////////////
        // Build static vegetation :
        for (AbstractEntity ent : ZoneGenerationUtils.appendRandomTerrainNonCollibableElements(
                FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600,
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                    StaticSpriteVars.mushrooms_brown_25x25),
                randomDefinitions.get(Tree.class.getSimpleName().toString())[0] * 32,
                randomDefinitions.get(Tree.class.getSimpleName().toString())[1] + 10)) {
            if (!CollisionUtils.inCollision(this.statics, ent)) {
                this.statics.put(String.valueOf(++index), ent);
            }
        }
        for (AbstractEntity ent : ZoneGenerationUtils.appendRandomTerrainNonCollibableElements(
                FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600,
                SpriteUtils.getSprite(Game.getInstance().getTextureLoader(), 
                    StaticSpriteVars.mushrooms_red_25x25),
                randomDefinitions.get(Tree.class.getSimpleName().toString())[0] * 42,
                randomDefinitions.get(Tree.class.getSimpleName().toString())[1] + 10)) {
            if (!CollisionUtils.inCollision(this.statics, ent)) {
                this.statics.put(String.valueOf(++index), ent);
            }
        }
        
        ////////////////////////////////////////////////////////////////////////
        // Trim collisions between param AbstractEntity collection and globals :  
        iterator = this.globals.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            for (int i = 0; i < entities.length; ++i) {
                if (entities[i].collidesWith((AbstractEntity)pair.getValue())) {
                    this.globals.remove((String)pair.getKey());
                    break;
                }
            }
        }
        
    }

    /**
     * Access populated AbstractEntity hasp map.
     *
     * @return Map<String, AbstractEntity>
     * @throws formicarius.exceptions.ZoneBuildException
     */
    public Map<String, AbstractEntity> getGlobals() throws ZoneBuildException {
        if (this.globals == null) {
            throw new ZoneBuildException();
        }
        return globals;
    }
    
    /**
     * 
     * @return 
     * @throws formicarius.exceptions.ZoneBuildException 
     */
    public Map<String, AbstractEntity> getStatics() throws ZoneBuildException {
        if (this.statics == null) {
            throw new ZoneBuildException();
        }
        return statics;
    }

}
