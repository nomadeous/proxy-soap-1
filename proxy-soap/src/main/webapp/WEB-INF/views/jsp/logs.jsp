<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="org.jlm.tools.proxy.soap.frontend.FrontEndCallsCollector"%>
<%@page import="org.jlm.tools.proxy.soap.frontend.FrontEndCall"%>
<%@page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<h2>Logs</h2>
<table class="table table-bordered" id="logs">
    <thead>
        <tr>
            <th>Timestamp</th>
            <th>Target</th>
            <th>Validation XSD</th>
            <th>Request</th>
            <th>Response</th>
            <th>Response time (ms)</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="call" items="${applicationScope['collector'].calls}" varStatus="loop">
        <c:choose>
            <c:when test="${call.xsdValidation && call.requestValid && call.responseValid}">
                <c:set var="status" value="success" />
            </c:when>
            <c:when test="${not call.xsdValidation && call.requestValid && call.responseValid}">
                <c:set var="status" value="warning" />
            </c:when>
            <c:otherwise>
                <c:set var="status" value="danger" />
            </c:otherwise>
        </c:choose>
        
        <tr class="${status} text-center">
            <td>${call.date}</td>
            <td>${call.target}</td>
            <td>
                <c:choose>
                    <c:when test="${call.xsdValidation}">
                        <span class="label label-success">Active</span>
                    </c:when>
                    <c:otherwise>
                        <span class="label label-danger">Inactive</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${call.requestValid}">
                        <span class="label label-success">Valid</span>
                    </c:when>
                    <c:otherwise>
                        <span class="label label-danger">Invalid</span>
                    </c:otherwise>
                </c:choose>
                <a class="glyphicon glyphicon-file" data-toggle="modal" href="#reqModal_${loop.index}" title="view content"></a>
                <div class="modal fade" id="reqModal_${loop.index}">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                          <h4 class="modal-title">Request content</h4>
                        </div>
                        <div class="modal-body">
                            <p><c:out escapeXml="true" value="${call.request}" /></p>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                      </div><!-- /.modal-content -->
                    </div><!-- /.modal-dialog -->
                </div><!-- /.modal -->
            </td>
            <td>
                <c:choose>
                    <c:when test="${call.responseValid}">
                        <span class="label label-success">Valid</span>
                    </c:when>
                    <c:when test="${empty call.responseValid}">
                        <span class="label">No response</span>
                    </c:when>
                    <c:otherwise>
                        <span class="label label-danger">Invalid</span>
                    </c:otherwise>
                </c:choose>
                <a class="glyphicon glyphicon-file" data-toggle="modal" href="#respModal_${loop.index}" title="view content"></a>
                <div class="modal fade" id="respModal_${loop.index}">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                          <h4 class="modal-title">Response content</h4>
                        </div>
                        <div class="modal-body">
                            <p><c:out escapeXml="true" value="${call.response}" /></p>
                        </div>
                        <div class="modal-footer">
                          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                      </div><!-- /.modal-content -->
                    </div><!-- /.modal-dialog -->
                </div><!-- /.modal -->
            </td>
            <td>${call.proxyTime}</td>
        </tr>
        </c:forEach>
    </tbody>
    
        
</table>

