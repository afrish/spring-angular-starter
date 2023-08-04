import {AppComponent} from '../../../main/web/app/app.component';

describe('AppComponent', () => {
  it('mounts', () => {
    cy.mount(AppComponent);
  });

  it('should display welcome message', () => {
    cy.mount(AppComponent);
    cy.get('h1').contains('Welcome to Spring Angular Starter!');
  });

  it('should display running message', () => {
    cy.mount(AppComponent);
    cy.get('.content span').contains('The app is running');
  });

  it('should have Angular logo', () => {
    cy.mount(AppComponent);
    cy.get('img').should('have.attr', 'alt', 'Angular Logo');
  });

  it('should have links to Angular resources', () => {
    cy.mount(AppComponent);

    cy.get('a').should('have.length', 3);

    const tourOfHeroesLink = cy.get('a').eq(0);
    tourOfHeroesLink.should('have.text', 'Tour of Heroes');
    tourOfHeroesLink.should('have.attr', 'href', 'https://angular.io/tutorial');
    tourOfHeroesLink.should('have.attr', 'target', '_blank');

    const cliDocumentationLink = cy.get('a').eq(1);
    cliDocumentationLink.should('have.text', 'CLI Documentation');
    cliDocumentationLink.should('have.attr', 'href', 'https://angular.io/cli');
    cliDocumentationLink.should('have.attr', 'target', '_blank');

    const angularBlogLink = cy.get('a').eq(2);
    angularBlogLink.should('have.text', 'Angular blog');
    angularBlogLink.should('have.attr', 'href', 'https://blog.angular.io/');
    angularBlogLink.should('have.attr', 'target', '_blank');
  });
});
