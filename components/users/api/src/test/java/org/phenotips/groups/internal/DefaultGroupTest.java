/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/
 */
package org.phenotips.groups.internal;

import org.phenotips.components.ComponentManagerRegistry;
import org.phenotips.groups.Group;
import org.phenotips.groups.GroupManager;

import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.util.ReflectionUtils;
import org.xwiki.model.reference.DocumentReference;
import org.xwiki.query.QueryException;
import org.xwiki.users.User;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Provider;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.xpn.xwiki.web.Utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Tests for the default {@link Group} implementation, {@link DefaultGroup}.
 *
 * @version $Id$
 */
public class DefaultGroupTest
{
    @Mock
    private ComponentManager cm;

    @Mock
    private Provider<ComponentManager> mockProvider;

    @Mock
    private GroupManager groupManager;

    /** Basic tests for {@link DefaultGroup#getReference()}. */
    @Test
    public void getReference() throws ComponentLookupException, QueryException
    {
        Group g = new DefaultGroup(null);
        Assert.assertNull(g.getReference());

        DocumentReference a = new DocumentReference("xwiki", "Groups", "Group A");
        Assert.assertEquals(a, new DefaultGroup(a).getReference());
    }

    /** There's a nicer toString implementation showing the group name. */
    @Test
    public void toStringTest()
    {
        DocumentReference a = new DocumentReference("xwiki", "Groups", "Group A");
        Assert.assertTrue(new DefaultGroup(a).toString().contains("Group A"));
    }

    @Test
    public void equalsTest()
    {
        DocumentReference a1 = new DocumentReference("xwiki", "Groups", "Group A");
        DocumentReference a2 = new DocumentReference("xwiki", "Groups", "Group A");
        DocumentReference a3 = new DocumentReference("xwiki", "Groups", "Group B");
        Assert.assertTrue(new DefaultGroup(a1).equals(new DefaultGroup(a2)));
        Assert.assertFalse(new DefaultGroup(a1).equals(new DefaultGroup(a3)));
        Assert.assertFalse(new DefaultGroup(a1).equals("not a group"));
    }

    @Test
    public void hashCodeTest()
    {
        DocumentReference a = new DocumentReference("xwiki", "Groups", "group A");
        Assert.assertTrue(new DefaultGroup(a).hashCode() == "group A".hashCode());
        Assert.assertFalse(new DefaultGroup(a).hashCode() == "aaa".hashCode());
    }

    @Test
    public void isUserInGroupTest() throws ComponentLookupException, QueryException
    {
        MockitoAnnotations.initMocks(this);
        Utils.setComponentManager(this.cm);
        ReflectionUtils.setFieldValue(new ComponentManagerRegistry(), "cmProvider", this.mockProvider);
        when(this.mockProvider.get()).thenReturn(this.cm);

        DocumentReference docref = new DocumentReference("xwiki", "Groups", "Group A");
        DefaultGroup groupA = new DefaultGroup(docref);
        User u = mock(User.class);

        when(this.cm.getInstance(GroupManager.class)).thenReturn(this.groupManager);

        when(groupManager.getGroupsForUser(u)).thenReturn(null);
        Assert.assertFalse(groupA.isUserInGroup(u));

        Set<Group> groups = new HashSet<Group>();
        when(groupManager.getGroupsForUser(u)).thenReturn(groups);
        Assert.assertFalse(groupA.isUserInGroup(u));

        groups.add(groupA);
        Assert.assertTrue(groupA.isUserInGroup(u));

        when(this.cm.getInstance(GroupManager.class)).thenThrow(new ComponentLookupException("failed"));
        boolean fails = false;
        try {
            groupA.isUserInGroup(u);
        } catch (NullPointerException e) {
            fails = true;
        }
        Assert.assertTrue(fails);
    }

    @Test
    public void getAllUserNamesTest() throws Exception
    {
        // GroupManager groupManagerTest = this.mocker.getComponentUnderTest();
        //
        // XWikiDocument groupXDocument = mock(XWikiDocument.class);
        // DocumentReference groupReference = mock(DocumentReference.class);
        // Group group = mock(Group.class);
        // when(group.getReference()).thenReturn(groupReference);
        //
        // DocumentAccessBridge bridge = this.mocker.getInstance(DocumentAccessBridge.class);
        // when(bridge.getDocument(groupReference)).thenReturn(groupXDocument);
        // when(groupXDocument.getXObjects(MEMBERS_REFERENCE)).thenReturn(null);
        // org.junit.Assert.assertTrue(groupManagerTest.getAllUserNames(group).isEmpty());
        //
        // DocumentReferenceResolver<String> resolver =
        // this.mocker.getInstance(DocumentReferenceResolver.TYPE_STRING, "current");
        // List<BaseObject> membersList = new LinkedList<BaseObject>();
        // when(groupXDocument.getXObjects(MEMBERS_REFERENCE)).thenReturn(membersList);
        // org.junit.Assert.assertTrue(groupManagerTest.getAllUserNames(group).isEmpty());
        //
        // BaseObject base1 = mock(BaseObject.class);
        // StringProperty st1 = mock(StringProperty.class);
        // when(base1.getField("member")).thenReturn(st1);
        // when(st1.getValue()).thenReturn("");
        // membersList.add(base1);
        //
        // BaseObject base2 = mock(BaseObject.class);
        // StringProperty st2 = mock(StringProperty.class);
        // when(base2.getField("member")).thenReturn(st2);
        // when(st2.getValue()).thenReturn("user");
        // when(resolver.resolve("user", GROUP_SPACE)).thenReturn(null);
        // membersList.add(base2);
        //
        // BaseObject base3 = mock(BaseObject.class);
        // StringProperty st3 = mock(StringProperty.class);
        // when(base3.getField("member")).thenReturn(st3);
        // when(st3.getValue()).thenReturn("group");
        // DocumentReference subGroup = mock(DocumentReference.class);
        // when(resolver.resolve("group", GROUP_SPACE)).thenReturn(subGroup);
        // membersList.add(base3);
        //
        // org.junit.Assert.assertEquals(1, groupManagerTest.getAllUserNames(group).size());
    }

}
