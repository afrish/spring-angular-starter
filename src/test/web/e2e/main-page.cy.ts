describe('Main page test', () => {
  it('should display welcome message', () => {
    cy.visit('/');
    cy.contains('Welcome to Spring Angular Starter!');
  });
});
