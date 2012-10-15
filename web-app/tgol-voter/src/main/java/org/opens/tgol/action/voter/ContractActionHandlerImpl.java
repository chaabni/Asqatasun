/*
 * Tanaguru - Automated webpage assessment
 * Copyright (C) 2008-2011  Open-S Company
 *
 * This file is part of Tanaguru.
 *
 * Tanaguru is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: open-s AT open-s DOT com
 */
package org.opens.tgol.action.voter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.opens.tgol.action.Action;
import org.opens.tgol.action.builder.ActionBuilder;
import org.opens.tgol.entity.contract.Contract;
import org.opens.tgol.entity.functionality.Functionality;

/**
 *
 * @author jkowalczyk
 */
public class ContractActionHandlerImpl implements ActionHandler {

    private List<ActionBuilder> actionBuilderList;

    public final void setActionBuilderList(List<ActionBuilder> actionBuilderList) {
        this.actionBuilderList = actionBuilderList;

    }

    @SuppressWarnings("unchecked")
    public List<Action> getActionList() {
        List<Action> actionList = new ArrayList<Action>();
        for (ActionBuilder actionBuilder : actionBuilderList) {
            actionList.add(actionBuilder.build());
        }
        return actionList;
    }

    @Override
    public synchronized List<Action> getActionList(
            Object object) {
        if (! (object instanceof Contract)) {
            return null;
        }
        List<Action> userContractActionList = getActionList();
        Collection<Functionality> functionalitySet = 
                ((Contract)object).getFunctionalitySet();
        for (Action action : userContractActionList) {
            activateAction(action, functionalitySet);
        }
        return userContractActionList;
    }

    /**
     * If the code handled by the action corresponds to one Functionality 
     * associated with the contract, this action is enabled. Otherwise, the action
     * is disabled
     * 
     * @param action
     * @param functionalitySet
     * @return
     *      the ContractActionVoter regarding the product
     */
    private void activateAction(
            Action action,
            Collection<Functionality> functionalitySet) {
        for (Functionality functionality : functionalitySet) {
            if (StringUtils.equals(action.getActionCode(), functionality.getCode())) {
                action.setActionEnabled(true);
                return;
            }
        }
        action.setActionEnabled(false);
    }
    
}