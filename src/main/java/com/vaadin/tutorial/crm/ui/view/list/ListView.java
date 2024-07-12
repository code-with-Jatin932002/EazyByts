package com.vaadin.tutorial.crm.ui.view.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.entity.Company;
import com.vaadin.tutorial.crm.backend.entity.Contact;
import com.vaadin.tutorial.crm.backend.service.CompanyService;
import com.vaadin.tutorial.crm.backend.service.ContactService;
import com.vaadin.tutorial.crm.ui.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Route(value = "", layout = MainLayout.class)
@PageTitle("Contacts | CRM By Jaffar")
public class ListView extends VerticalLayout {
    ContactService contactService;
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm contactForm;
    Button addContact = new Button("Add Contact");

    public ListView(ContactService contactService, CompanyService companyService) {
        this.contactService = contactService;

        addClassName("list-view");
        setSizeFull();

        configureFilter();
        configureAddContactButton();
        configureGrid();

        contactForm = new ContactForm(companyService.findAll());
        contactForm.addListener(ContactForm.SaveEvent.class, this::saveContact);
        contactForm.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        contactForm.addListener(ContactForm.CloseEvent.class, e->closeEditor());

        Div content = new Div(grid, contactForm);
        content.addClassName("content");
        content.setSizeFull();

        add(new HorizontalLayout(filterText, addContact), content);

        updateList();

        closeEditor();
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        grid.removeColumnByKey("company");
        grid.setColumns("firstName", "lastName", "email", "status");

        grid.addColumn(contact -> {
            Company company = contact.getCompany();
            return company == null ? "-" : company.getName();
        }).setHeader("Company");

        grid.getColumns().forEach(columns -> columns.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    private void updateList(){
        grid.setItems(contactService.findAll(filterText.getValue()));
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            contactForm.setContact(contact);
            contactForm.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        // contactForm.setContact(null); TODO: remove this line, I think code works fine without this line
        contactForm.setVisible(false);
        removeClassName("editing");
    }

    private void saveContact(ContactForm.SaveEvent event) {
        contactService.save(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        contactService.delete(event.getContact());
        updateList();
        closeEditor();
    }

    private void configureAddContactButton() {
        addContact.addClickListener(event -> {
            contactForm.setVisible(true);
            addClassName("editing");
            editContact(new Contact());
        });
    }
}