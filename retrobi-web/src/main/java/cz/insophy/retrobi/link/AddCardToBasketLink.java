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

import java.util.Collections;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.model.Model;

import cz.insophy.retrobi.RetrobiWebSession;
import cz.insophy.retrobi.exception.OverLimitException;
import cz.insophy.retrobi.pages.AbstractBasicPage;
import cz.insophy.retrobi.pages.BasketPage;
import cz.insophy.retrobi.utils.component.AjaxWaitLink;

/**
 * A link that adds the specified card to the basket after clicking.
 * 
 * @author Vojtěch Hordějčuk
 */
public class AddCardToBasketLink extends AjaxWaitLink<String> {
    /**
     * default serial version
     */
    private static final long serialVersionUID = 1L;
    /**
     * sibling link to be updated
     */
    private AbstractLink sibling;
    
    /**
     * Creates a new instance.
     * 
     * @param id
     * component ID
     * @param cardId
     * ID of a card to be added when clicked
     */
    public AddCardToBasketLink(final String id, final String cardId) {
        super(id, Model.of(cardId));
        this.sibling = null;
    }
    
    @Override
    public boolean isEnabled() {
        if (RetrobiWebSession.get().getCardContainer().isInBasket(this.getModelObject())) {
            // disable if the card is in the basket already
            
            return false;
        }
        
        if (!(RetrobiWebSession.get().getBasketLimit() < 0)) {
            if (RetrobiWebSession.get().getCardContainer().getBasketSize() >= RetrobiWebSession.get().getBasketLimit()) {
                // disable if the basket is full
                
                return false;
            }
        }
        
        return super.isEnabled();
    }
    
    /**
     * Sets the sibling link for update.
     * 
     * @param link
     * sibling link
     */
    public void setSiblingLink(final AbstractLink link) {
        this.sibling = link;
    }
    
    @Override
    public void onClick(final AjaxRequestTarget target) {
        // add card to the basket
        
        try {
            RetrobiWebSession.get().getCardContainer().addToBasket(
                    Collections.singleton(this.getModelObject()),
                    RetrobiWebSession.get().getBasketLimit());
        } catch (final OverLimitException x) {
            this.error(x.getMessage());
        }
        
        // refresh the page if currently on basket page
        // (this is because ranges are very hard to update via AJAX)
        
        if (this.getPage() instanceof BasketPage) {
            this.setRedirect(true);
            this.setResponsePage(this.getPage());
            target.respond(this.getRequestCycle());
            return;
        }
        
        // mark listeners for update
        
        if (target != null) {
            target.addComponent(this);
            
            if (this.sibling != null) {
                target.addComponent(this.sibling);
            }
            
            if (this.getPage() instanceof AbstractBasicPage) {
                ((AbstractBasicPage) this.getPage()).modifyAjaxTarget(target);
            }
        }
    }
}
