package com.example.shop.ui.views.payment;

import com.example.shop.backend.models.Transaction;
import com.example.shop.backend.services.TransactionService;
import com.example.shop.common.constants.StringConstants;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.server.StreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * TransactionListView class represents a view for displaying transactions with filtering and search capabilities.
 */
@CssImport("./styles/transactions-view.css")
public class TransactionListView extends VerticalLayout {

    private final TransactionService transactionService;
    private final Grid<Transaction> grid;
    private String filter = StringConstants.ALL;
    private String searchTerm = "";

    /**
     * Constructor for TransactionListView initializes the view with transaction service and sets up UI components.
     *
     * @param transactionService Service for managing transactions
     */
    public TransactionListView(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.grid = createGrid();

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        // Add header, filters, and grid to the view
        add(createHeader());
        add(createFilters());
        add(grid);

        addClassName("transaction-list-view");
    }

    /**
     * Creates a header component for the transaction list view.
     *
     * @return Component representing the header
     */
    private Component createHeader() {
        H2 title = new H2(StringConstants.TRANSACTIONS);
        title.addClassName("transactions-title");
        return title;
    }

    /**
     * Creates a horizontal layout with filter buttons and search/download section.
     *
     * @return HorizontalLayout with filter and search/download components
     */
    private HorizontalLayout createFilters() {
        HorizontalLayout filterBar = new HorizontalLayout();
        filterBar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        filterBar.addClassName("filter-bar");

        // Filter Buttons
        HorizontalLayout filterButtons = getHorizontalLayout();

        // Search and Download
        TextField searchField = getSearchField();
        HorizontalLayout searchAndDownload = searchAndDownloadLayer(searchField);

        filterBar.add(filterButtons, searchAndDownload);
        return filterBar;
    }

    /**
     * Creates a TextField component for searching transactions.
     *
     * @return TextField for searching transactions
     */
    private TextField getSearchField() {
        TextField searchField = new TextField();
        searchField.setPlaceholder(StringConstants.SEARCH);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.isClearButtonVisible();
        searchField.setValueChangeMode(ValueChangeMode.LAZY);

        // Listener to handle value changes in the search field
        searchField.addValueChangeListener(event -> {
            searchTerm = event.getValue().trim();
            grid.getDataProvider().refreshAll();
        });
        searchField.addClassName("search-field");
        return searchField;
    }

    /**
     * Creates a horizontal layout with search field and download button.
     *
     * @param searchField TextField component for searching transactions
     * @return HorizontalLayout with search field and download button
     */
    private HorizontalLayout searchAndDownloadLayer(TextField searchField) {
        Button downloadButton = new Button(StringConstants.DOWNLOAD, new Icon(VaadinIcon.DOWNLOAD_ALT));
        downloadButton.addClassName("download-button");

        // Listener to handle download button click
        downloadButton.addClickListener(event -> {
            // Create a StreamResource for downloading CSV data
            StreamResource resource = new StreamResource("transactions.csv", this::getCsvInputStream);
            Anchor anchor = new Anchor(resource, "Download");
            anchor.getElement().setAttribute("download", true);
            anchor.setId("download-csv-anchor");
            add(anchor);
            anchor.getElement().executeJs("document.getElementById('download-csv-anchor').click();")
                    .then(a -> remove(anchor));
        });

        HorizontalLayout searchAndDownload = new HorizontalLayout(searchField, downloadButton);
        searchAndDownload.addClassName("search-download");
        return searchAndDownload;
    }

    /**
     * Retrieves the CSV input stream for exporting transaction data.
     *
     * @return InputStream containing CSV data
     */
    private InputStream getCsvInputStream() {
        String csvData = transactionService.generateCsvData(filter);
        return new ByteArrayInputStream(csvData.getBytes());
    }

    /**
     * Creates a horizontal layout with filter buttons for transaction status filtering.
     *
     * @return HorizontalLayout with filter buttons
     */
    private HorizontalLayout getHorizontalLayout() {
        // Consumer to handle filter button clicks
        Consumer<String> filterConsumer = status -> {
            filter = status;
            grid.getDataProvider().refreshAll();
        };

        // Create filter buttons for different transaction statuses
        Button allButton = createFilterButton(StringConstants.ALL, filterConsumer);
        Button completedButton = createFilterButton(StringConstants.COMPLETED, filterConsumer);
        Button pendingButton = createFilterButton(StringConstants.PENDING, filterConsumer);
        Button failedButton = createFilterButton(StringConstants.FAILED, filterConsumer);

        HorizontalLayout filterButtons = new HorizontalLayout(allButton, completedButton, pendingButton, failedButton);
        filterButtons.addClassName("filter-buttons");
        return filterButtons;
    }

    /**
     * Creates a Button component for a transaction status filter.
     *
     * @param text           Text to display on the filter button
     * @param filterConsumer Consumer to handle filter button click
     * @return Button component representing the filter button
     */
    private Button createFilterButton(String text, Consumer<String> filterConsumer) {
        Button button = new Button(text);
        button.addClassName("filter-button");

        // Add selected/unselected CSS class based on current filter status
        if (filter.toLowerCase().equals(text)) {
            button.addClassName("selected");
        } else {
            button.addClassName("unselected");
        }

        // Listener to handle filter button click
        button.addClickListener(event -> filterConsumer.accept(text));
        return button;
    }

    /**
     *
     * Creates a Grid component for displaying transaction data.
     * @return Grid<Transaction> component for transaction data
     */
    private Grid<Transaction> createGrid() {
        Grid<Transaction> grid = new Grid<>(Transaction.class, false);
        grid.addColumn(Transaction::getTransactionId).setHeader(StringConstants.TRANSACTION_ID);
        grid.addColumn(Transaction::getProduct).setHeader(StringConstants.PRODUCT);
        grid.addColumn(Transaction::getAmount).setHeader(StringConstants.AMOUNT);
        grid.addColumn(Transaction::getBuyerEmail).setHeader(StringConstants.BUYER_INFORMATION);
        grid.addColumn(new ComponentRenderer<>(this::createStatusBadge)).setHeader(StringConstants.STATUS);
        grid.addColumn(Transaction::getDate).setHeader(StringConstants.DATE);

        grid.setPageSize(10); // Set number of items per page

        // DataProvider to fetch and provide transaction data
        DataProvider<Transaction, Void> dataProvider = DataProvider.fromCallbacks(
                query -> {
                    int offset = query.getOffset();
                    int limit = query.getLimit();
                    Pageable pageable = PageRequest.of(offset / limit, limit);

                    Page<Transaction> page;
                    if (filter.equals(StringConstants.ALL)) {
                        page = transactionService.getTransactions(pageable);
                    } else {
                        page = transactionService.getTransactionsByStatus(filter, pageable);
                    }
                    return page.getContent().stream();
                },
                query -> {
                    if (filter.equals(StringConstants.ALL)) {
                        if (searchTerm.isEmpty()) {
                            return (int) transactionService.countTransactions();
                        } else {
                            return (int) transactionService.searchTransactions(searchTerm, PageRequest.of(0, 1)).getTotalElements();
                        }
                    } else {
                        if (searchTerm.isEmpty()) {
                            return (int) transactionService.countTransactionsByStatus(filter);
                        } else {
                            return (int) transactionService.searchTransactions(searchTerm, PageRequest.of(0, 1)).getTotalElements();
                        }
                    }
                }
        );

        grid.setDataProvider(dataProvider);
        grid.addClassName("transactions-grid");

        return grid;
    }

    /**
     * Creates a status badge component for displaying transaction status.
     *
     * @param transaction Transaction object representing a transaction
     * @return Component representing the status badge
     */
    private Component createStatusBadge(Transaction transaction) {
        Span badge = new Span(transaction.getStatus().toString());
        badge.addClassName("status-badge");
        badge.addClassName(transaction.getStatus().toString().toLowerCase());
        return badge;
    }
}
