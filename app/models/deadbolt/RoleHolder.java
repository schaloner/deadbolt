/*
 * Copyright 2010-2011 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models.deadbolt;

import java.util.List;

/**
 * Implementers of this interface should represent whatever the application considers to be the current user.
 *
 * @author Steve Chaloner (steve@objectify.be).
 */
public interface RoleHolder
{
    /**
     * A list of {@link Role}s held by the role holder.
     *
     * @return a list of roles
     */
    List<? extends Role> getRoles();
}
