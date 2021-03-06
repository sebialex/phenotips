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
package org.phenotips.data;

import org.xwiki.component.annotation.Role;
import org.xwiki.stability.Unstable;

/**
 * Component role used for inserting data into a new patient record.
 *
 * @version $Id$
 * @since 1.0M10
 */
@Unstable
@Role
public interface PatientRecordInitializer
{
    /**
     * The function that is automatically invoked when a new patient record is created and should contain code that
     * inserts data into the record.
     *
     * @param patient the newly created patient
     */
    void initialize(Patient patient);
}
