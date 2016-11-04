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
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.singleton;

public class MeasureTreeQuery {

  @CheckForNull
  private final String analysisUuid;

  @CheckForNull
  private final Collection<Integer> metricIds;

  @CheckForNull
  private final Collection<String> metricKeys;

  @CheckForNull
  private final Long personId;

  private MeasureTreeQuery(Builder builder) {
    this(builder.analysisUuid, builder.metricIds, builder.metricKeys, builder.personId);
  }

  private MeasureTreeQuery(@Nullable String analysisUuid,
    @Nullable Collection<Integer> metricIds,
    @Nullable Collection<String> metricKeys,
    @Nullable Long personId) {
    checkArgument(metricIds == null || metricKeys == null, "Metric IDs and keys must not be set both");
    this.analysisUuid = analysisUuid;
    this.metricIds = metricIds;
    this.metricKeys = metricKeys;
    this.personId = personId;
  }

  public String getAnalysisUuid() {
    return analysisUuid;
  }

  @CheckForNull
  public Collection<Integer> getMetricIds() {
    return metricIds;
  }

  @CheckForNull
  public Collection<String> getMetricKeys() {
    return metricKeys;
  }

  @CheckForNull
  public Long getPersonId() {
    return personId;
  }

  public boolean returnsEmpty() {
    return (metricIds != null && metricIds.isEmpty())
      || (metricKeys != null && metricKeys.isEmpty());
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private String analysisUuid;
    private Collection<Integer> metricIds;
    private Collection<String> metricKeys;
    private Long personId;

    private Builder() {
    }

    public Builder setAnalysisUuid(String analysisUuid) {
      this.analysisUuid = analysisUuid;
      return this;
    }

    /**
     * All the measures are returned if parameter is {@code null}.
     */
    public Builder setMetricIds(@Nullable Collection<Integer> metricIds) {
      this.metricIds = metricIds;
      return this;
    }

    public Builder setMetricId(int metricId) {
      this.metricIds = singleton(metricId);
      return this;
    }

    /**
     * All the measures are returned if parameter is {@code null}.
     */
    public Builder setMetricKeys(@Nullable Collection<String> s) {
      this.metricKeys = s;
      return this;
    }

    public Builder setMetricKey(String s) {
      this.metricKeys = singleton(s);
      return this;
    }

    public Builder setPersonId(@Nullable Long l) {
      this.personId = l;
      return this;
    }

    public MeasureTreeQuery build() {
      return new MeasureTreeQuery(this);
    }
  }
}
