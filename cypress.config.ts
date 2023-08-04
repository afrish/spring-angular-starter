import 'dotenv/config';
import {defineConfig} from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    supportFile: 'src/test/web/support/e2e.ts',
    specPattern: 'src/test/web/e2e/**/*.cy.ts',
    fixturesFolder: 'src/test/web/fixtures',
    downloadsFolder: 'target/cypress/downloads',
    screenshotsFolder: 'target/cypress/screenshots',
    videosFolder: 'target/cypress/videos',
    video: false,
    screenshotOnRunFailure: false,
    watchForFileChanges: false,

    setupNodeEvents(on: Cypress.PluginEvents, config: Cypress.PluginConfigOptions) {
      const baseUrl = process.env['CYPRESS_BASE_URL'];
      if (baseUrl) {
        config.baseUrl = baseUrl;
      }
      return config;
    }
  },

  component: {
    devServer: {
      framework: 'angular', bundler: 'webpack',
    },
    video: false,
    screenshotOnRunFailure: false,
    indexHtmlFile: 'src/test/web/support/component-index.html',
    supportFile: 'src/test/web/support/component.ts',
    specPattern: 'src/test/web/component/**/*.cy.ts'
  }

});
