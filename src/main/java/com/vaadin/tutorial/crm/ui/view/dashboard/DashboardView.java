package com.vaadin.tutorial.crm.ui.view.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.tutorial.crm.backend.service.CompanyService;
import com.vaadin.tutorial.crm.backend.service.ContactService;
import com.vaadin.tutorial.crm.ui.MainLayout;

import java.util.Map;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Contacts | CRM By Jaffar")
public class DashboardView extends VerticalLayout {
    private CompanyService companyService;
    private ContactService contactService;

    public DashboardView(CompanyService companyService, ContactService contactService){
        this.companyService = companyService;
        this.contactService = contactService;

        add(getContactStats(), getCompaniesChart());

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private Component getContactStats(){
        Span stats = new Span(contactService.count() + " contacts");
        stats.addClassName("contact-stats");
        return stats;
    }

    private Chart getCompaniesChart() {
        Chart pieChart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();

        Map<String, Integer> employeePerCompaniesStats = companyService.getEmployeeStatsPerCompany();
        employeePerCompaniesStats.forEach( (company, employeeCount) ->
                dataSeries.add(new DataSeriesItem(company, employeeCount))
        );

        pieChart.getConfiguration().setSeries(dataSeries);
        return pieChart;
    }
}
