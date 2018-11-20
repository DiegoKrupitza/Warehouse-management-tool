<%@ page import="domain.Artikel" %>
<%@ page import="java.util.List" %>
<%
    List<Artikel> artikelList = (List<Artikel>) request.getAttribute("allArtikel");
    for (Artikel artikel : artikelList
            ) {

%>

<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
    <div class="card">
        <% if (artikel.getInStock() < 10 && artikel.getInStock() > 3) { %>
        <div class="warning-need-to-buy-more">You need more</div>
        <% } %>
        <% if (artikel.getInStock() <= 3) { %>
        <div class="need-to-buy-more">Buy More</div>
        <% } %>
        <div class="header">
            <h3 class="title"><% out.print(artikel.getName()); %><br>
                <small class="type-info"><% out.print(artikel.getHersteller()); %></small>
            </h3>
        </div>
        <div class="content">
            <img src="/img/articelImages/<% out.print(artikel.getBildUrl()); %>" width="100%" height="300px">
            <p><span class="product-info">In stock: </span><span><% out.print(artikel.getInStock()); %></span></p>
            <p><span class="product-info">Price: </span><span><% out.print(artikel.getPreis()); %></span></p>
            <p><span class="product-info">Kategorie: </span><span><% out.print(artikel.getKategorie().name()); %></span>
            </p>
            <div class="more-info" id="more-info-<% out.print(artikel.getId()); %>">
                <p class="product-info">Description:    </p>
                <p><% out.print(artikel.getDescription()); %></p>
            </div><!-- more-info -->
            <div class="footer articel-footer text-center">
                <div class="col-xs-4 text-center">
                    <a href="/articel/edit/<% out.print(artikel.getId()); %>"><i
                            class="ti-pencil-alt2 icon-warning"></i>
                    </a>
                </div>

                <div class="col-xs-4 text-center">
                    <a href="#" id="animated-more-info" data-id="<% out.print(artikel.getId()); %>"><i
                            class="ti-eye"></i>
                    </a>
                </div>

                <div class="col-xs-4 text-center">
                    <a href="#" id="delete-action-link" data-name="<% out.print(artikel.getName()); %>"
                       data-id="<% out.print(artikel.getId()); %>"><i class="ti-trash icon-danger"></i></a>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>
</div>
<%
    }
%>