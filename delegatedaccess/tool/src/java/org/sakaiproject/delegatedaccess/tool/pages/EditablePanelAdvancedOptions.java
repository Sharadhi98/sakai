/*
* The Trustees of Columbia University in the City of New York
* licenses this file to you under the Educational Community License,
* Version 2.0 (the "License"); you may not use this file
* except in compliance with the License. You may obtain a copy of the
* License at:
*
* http://opensource.org/licenses/ecl2.txt
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.sakaiproject.delegatedaccess.tool.pages;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.sakaiproject.delegatedaccess.model.NodeModel;
import org.sakaiproject.delegatedaccess.util.DelegatedAccessConstants;

public class EditablePanelAdvancedOptions extends Panel{

	private NodeModel nodeModel;
	private TreeNode node;
	private boolean loadedFlag = false;
	
	public EditablePanelAdvancedOptions(String id, IModel inputModel, final NodeModel nodeModel, final TreeNode node, final int userType){
		super(id);
		
		this.nodeModel = nodeModel;
		this.node = node;
		
		final WebMarkupContainer advancedOptionsSpan = new WebMarkupContainer("advancedOptionsSpan");
		advancedOptionsSpan.setOutputMarkupId(true);
		final String editableSpanId = advancedOptionsSpan.getMarkupId();
		add(advancedOptionsSpan);
		

		AjaxLink<Void> saveEditableSpanLink = new AjaxLink<Void>("saveEditableSpanLink") {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				target.appendJavaScript("document.getElementById('" + editableSpanId + "').style.display='none';");
				//In order for the models to refresh, you have to call "expand" or "collapse" then "updateTree",
				//since I don't want to expand or collapse, I just call whichever one the node is already
				//Refreshing the tree will update all the models and information (like role) will be generated onClick
				if(((BaseTreePage)target.getPage()).getTree().getTreeState().isNodeExpanded(node)){
					((BaseTreePage)target.getPage()).getTree().getTreeState().expandNode(node);
				}else{
					((BaseTreePage)target.getPage()).getTree().getTreeState().collapseNode(node);
				}
				((BaseTreePage)target.getPage()).getTree().updateTree(target);
			}
		};
		advancedOptionsSpan.add(saveEditableSpanLink);

		Label editableSpanLabel = new Label("editableNodeTitle", nodeModel.getNode().title);
		advancedOptionsSpan.add(editableSpanLabel);
		
		
		AjaxLink<Void> restrictToolsLink = new AjaxLink<Void>("advancedOptionsLink"){
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				target.appendJavaScript("document.getElementById('" + editableSpanId + "').style.display='';");
			}
		};
		add(restrictToolsLink);
		
		
		Label advancedOptionsSpanLabel = new Label("advancedOptionsSpan", new StringResourceModel("advanced"));
		restrictToolsLink.add(advancedOptionsSpanLabel);
		
		Label advnacedOptionsTitle = new Label("advnacedOptionsTitle", new StringResourceModel("advancedOptionsTitle"));
		advancedOptionsSpan.add(advnacedOptionsTitle);
		
		Label advancedOptionsInstructions = new Label("advancedOptionsInstructions", new StringResourceModel("advancedOptionsDesc"));
		advancedOptionsSpan.add(advancedOptionsInstructions);
		
		advancedOptionsSpan.add(new EditablePanelCheckbox("editablePanelCheckbox", new PropertyModel(node,  "userObject.shoppingPeriodRevokeInstructorEditable"), (NodeModel) ((DefaultMutableTreeNode) node).getUserObject(), node, DelegatedAccessConstants.TYPE_ADVANCED_OPT));
		advancedOptionsSpan.add(new EditablePanelCheckbox("revokePublicOptCheckbox", new PropertyModel(node,  "userObject.shoppingPeriodRevokeInstructorPublicOpt"), (NodeModel) ((DefaultMutableTreeNode) node).getUserObject(), node, DelegatedAccessConstants.TYPE_ADVANCED_OPT));
	}
	
}
