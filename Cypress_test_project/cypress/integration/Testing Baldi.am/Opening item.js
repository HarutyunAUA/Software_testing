describe('Choosing item from the menu', function (){
    it('Opening category from menu', function(){
        const originalURL="https://baldi.am/am_am/"
        cy.visit(originalURL)
        cy.get(':nth-child(2) > .menu > :nth-child(1) > a').click()
        cy.get('.home > a').click().url().should('eq', originalURL)
    })

    it('Opening item from the category', function (){
        cy.visit("https://baldi.am/am_am/")
        cy.get(':nth-child(2) > .menu > :nth-child(1) > a').click();
        const nameMenuPage=':nth-child(1) > .details > .product > .product-item-link'
        cy.get(nameMenuPage)
            .invoke('text')
            .then((val1) => {
                cy.get(nameMenuPage).click()
                cy.get('.product-name')
                    .invoke('text')
                    .should((val2) => {
                        expect(val1).contains(val2);
                    })
            })
    })
    it('Checking the navigation is correct', function (){
        cy.visit("https://baldi.am/am_am/")
        const category=':nth-child(2) > .menu > :nth-child(1) > a'
        cy.get(category)
            .invoke('text')
            .then((val1) => {
                cy.get(category).click()
                cy.get('.base')
                    .invoke('text')
                    .should((val2) => {
                        expect(val1).contains(val2);
                    })
            })
    })
})