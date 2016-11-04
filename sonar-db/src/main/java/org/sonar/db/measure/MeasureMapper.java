/*
 * SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.db.measure;

import java.util.Collection;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.sonar.db.component.ComponentTreeQuery;

public interface MeasureMapper {

  List<MeasureDto> selectByQueryOnProjects(@Param("measureQuery") MeasureQuery query);

  List<MeasureDto> selectByQueryOnComponents(@Param("measureQuery") MeasureQuery query);

  List<MeasureDto> selectByQueryOnSingleComponent(@Param("measureQuery") MeasureQuery query);

  void selectByQueryOnProjects(@Param("measureQuery") MeasureQuery query, ResultHandler resultHandler);

  void selectByQueryOnComponents(@Param("measureQuery") MeasureQuery query, ResultHandler resultHandler);

  void selectByQueryOnSingleComponent(@Param("measureQuery") MeasureQuery query, ResultHandler resultHandler);

  List<MeasureDto> selectTreeByQuery(@Param("measureQuery") MeasureTreeQuery measureQuery, @Param("query") ComponentTreeQuery componentTreeQuery,
    @Param("baseUuidPath") String baseUuidPath);

  List<PastMeasureDto> selectPastMeasures(@Param("componentUuid") String componentUuid, @Param("analysisUuid") String analysisUuid, @Param("metricIds") List<Integer> metricIds);

  List<MeasureDto> selectProjectMeasuresOfDeveloper(@Param("developerId") long developerId, @Param("metricIds") Collection<Integer> metricIds);

  void insert(MeasureDto measureDto);
}
