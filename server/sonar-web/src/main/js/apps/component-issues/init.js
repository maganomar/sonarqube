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
import $ from 'jquery';
import _ from 'underscore';
import Backbone from 'backbone';
import Marionette from 'backbone.marionette';
import State from '../issues/models/state';
import Layout from '../issues/layout';
import Issues from '../issues/models/issues';
import Facets from '../../components/navigator/models/facets';
import Filters from '../issues/models/filters';
import Controller from '../issues/controller';
import Router from '../issues/router';
import WorkspaceListView from '../issues/workspace-list-view';
import WorkspaceHeaderView from '../issues/workspace-header-view';
import FacetsView from './../issues/facets-view';

const App = new Marionette.Application();
const init = function (el) {
  const options = window.sonarqube;

  this.config = options.config;
  this.state = new State({
    isContext: true,
    contextQuery: { componentUuids: options.config.resource },
    contextComponentUuid: options.config.resource,
    contextComponentName: options.config.resourceName,
    contextComponentQualifier: options.config.resourceQualifier
  });
  this.updateContextFacets();
  this.list = new Issues();
  this.facets = new Facets();
  this.filters = new Filters();

  this.layout = new Layout({ app: this, el });
  this.layout.render();
  $('#footer').addClass('search-navigator-footer');

  this.controller = new Controller({ app: this });

  this.issuesView = new WorkspaceListView({
    app: this,
    collection: this.list
  });
  this.layout.workspaceListRegion.show(this.issuesView);
  this.issuesView.bindScrollEvents();

  this.workspaceHeaderView = new WorkspaceHeaderView({
    app: this,
    collection: this.list
  });
  this.layout.workspaceHeaderRegion.show(this.workspaceHeaderView);

  this.facetsView = new FacetsView({
    app: this,
    collection: this.facets
  });
  this.layout.facetsRegion.show(this.facetsView);

  this.controller.fetchFilters().done(function () {
    key.setScope('list');
    App.router = new Router({ app: App });
    Backbone.history.start();
  });
};

App.getContextQuery = function () {
  return { componentUuids: this.config.resource };
};

App.getRestrictedFacets = function () {
  return {
    'TRK': ['projectUuids'],
    'BRC': ['projectUuids'],
    'DIR': ['projectUuids', 'moduleUuids', 'directories'],
    'DEV': ['authors'],
    'DEV_PRJ': ['projectUuids', 'authors']
  };
};

App.updateContextFacets = function () {
  const facets = this.state.get('facets');
  const allFacets = this.state.get('allFacets');
  const facetsFromServer = this.state.get('facetsFromServer');
  return this.state.set({
    facets,
    allFacets: _.difference(allFacets, this.getRestrictedFacets()[this.config.resourceQualifier]),
    facetsFromServer: _.difference(facetsFromServer, this.getRestrictedFacets()[this.config.resourceQualifier])
  });
};

App.on('start', function (el) {
  init.call(App, el);
});

export default function (el) {
  App.start(el);
}
