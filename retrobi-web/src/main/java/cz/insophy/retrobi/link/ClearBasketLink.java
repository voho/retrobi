/*
 * Copyright 2012 UCL AV CR v.v.i.
 *
 * This file is part of Retrobi.
 *
 * Retrobi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Retrobi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Retrobi. If not, see <http://www.gnu.org/licenses/>.
 */

package cz.insophy.retrobi.link;

import org.apache.wicket.markup.html.link.Link;

import cz.insophy.retrobi.RetrobiWebSession;

/**
 * A link that removes all cards from the basket after clicking.
 * 
 * @author Vojtěch Hordějčuk
 */
public class ClearBasketLink extends Link<Object> {
    /**
     * default serial version
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance.
     * 
     * @param id
     * component ID
     */
    public ClearBasketLink(final String id) {
        super(id);
    }
    
    @Override
    public void onClick() {
        // clear the whole basket
        
        RetrobiWebSession.get().getCardContainer().clearBasket();
        
        // notify user
        
        this.info("Schránka byla vyprázdněna.");
    }
}
