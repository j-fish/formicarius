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
package fr.com.jfish.jfgformicarius.formicarius.entities.tiles;

import fr.com.jfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jfish.jfgformicarius.formicarius.constants.MvtConst;
import fr.com.jfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import fr.com.jfish.jfgformicarius.formicarius.entities.characters.MainCharacter;
import fr.com.jfish.jfgformicarius.formicarius.exceptions.ZoneGenerationException;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jfish.jfgformicarius.formicarius.helpers.entities.maincharacter.MainCharacterCaveZoneHelper;
import fr.com.jfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticSpriteVars;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.utils.CollisionUtils;
import fr.com.jfish.jfgformicarius.formicarius.utils.ZoneGenerationUtils;
import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class CaveEntrance extends AbstractEntity implements CollidableObject {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Sprite size.
     */
    public static final int SPRT_WH = 60;

    /**
     *  Collision rectangle width & height.
     */
    private static final int COLLISION_RECTANGLE_WH = 4;
    
    /**
     *
     */
    public static final String REF = "cave_entrance";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * constructor.
     *
     * @param game
     * @param sprite
     * @param x
     * @param y
     */
    public CaveEntrance(final Game game, final Sprite sprite, final int x, final int y) {
        super(game, sprite, x, y, MvtConst.STILL, CaveEntrance.class.getSimpleName());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    @Override
    public void collideWith(final AbstractEntity other) {
        
        if (other instanceof MainCharacter
                && CollisionUtils.inCollision(((MainCharacter) other).getMinimumLowerRectangle(), 
                    this.getRectangle())) {

            /**
             * Then change environment for cave scenarios. Affect new
             * TransitionAction class to MainCharacter.
             *
             * @see TransitionAction
             * @see MainCharacterZoneHelper
             */
            
            /**
             * TODO : Make value sent to buildRandomCaveZones(int) dynamic.
             */
            try {
                ((MainCharacter) other).setTransitionAction(
                        new MainCharacterCaveZoneHelper((MainCharacter) other, game,
                                ZoneGenerationUtils.buildRandomCaveZones(33)));
            } catch (final ZoneGenerationException zge) {
                Logger.getLogger(CaveEntrance.class.getName()).log(Level.SEVERE, null, zge);
            }
            
            game.getEntityHelper().getTileEntities().clear();
            game.getEntityHelper().initTilingEntities(StaticSpriteVars.cave_ground1_400);
            game.getEntityHelper().getFader().fade();
            game.getSoundManager().playEffect(StaticSoundVars.spell6);
            other.setX((FrameConst.FRM_WIDTH / 2) - MainCharacter.SPRT_W / 2);
            other.setY((FrameConst.FRM_HEIGHT / 2) - MainCharacter.SPRT_H / 2);
            ((MainCharacter) other).getTransitionAction().triggerTransition(MvtConst.STILL);
        }
    }

    @Override
    public void doLogic() {
    }

    @Override
    public void beforeRender(final boolean force) {
    }

    @Override
    public void afterRender(final boolean force) {
        DrawingHelper.getInstance().getDrawableQueue().add(0, this);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(this.getX() + (CaveEntrance.SPRT_WH / 2) - (CaveEntrance.COLLISION_RECTANGLE_WH / 2),
                this.getY() + (CaveEntrance.SPRT_WH / 2) - (CaveEntrance.COLLISION_RECTANGLE_WH / 2), 
                CaveEntrance.COLLISION_RECTANGLE_WH, CaveEntrance.COLLISION_RECTANGLE_WH);
    }

    @Override
    public Rectangle getRectangle(final AbstractEntity entity) {
        return null;
    }
    //</editor-fold>

}
