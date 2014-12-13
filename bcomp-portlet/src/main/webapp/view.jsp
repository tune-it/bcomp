<%@ page contentType="text/html; charset=UTF-8" %>

<%@ page import="javax.portlet.RenderRequest" %>
<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ page import="com.liferay.portal.service.UserLocalServiceUtil" %>
<%@ page import="com.liferay.portal.theme.ThemeDisplay" %>
<%@ page import="ru.ifmo.cs.bcomp.portlet.BCompOpts" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />

<%
ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
String username = UserLocalServiceUtil.getUserById(themeDisplay.getUserId()).getScreenName();
%>

<%if (username.matches("^s[0-9]{6}$")) { %>

<%
BCompOpts opts = new BCompOpts(username);
%>

<h3>Задание</h3>
<ol>
	<li>Написать программу для БЭВМ реализующую формулу: <%= opts.formula %></li>
	<li>Проверить работу программы для следующих пар значений X и Y:
		<% for (int i = 0; i < opts.x.length; i++)
			out.println("<br>X = " + opts.x[i] + "; Y = " + opts.y[i] );
		%>
	</li>
</ol>


<h3>Отчёт должен включать в себя:</h3>
<ol>
	<li>Титульный лист</li>
	<li>Текст программы в форме, приведённой для 1-й лабораторной работы</li>
	<li>Описание программы:<ul>
			<li>назначение программы и реализуемая ей формула</li>
			<li>область представления исходных данных и результатов</li>
			<li>расположение в памяти ЭВМ программы, исходных данных и результатов</li>
			<li>адреса первой и последней выполняемых команд программы</li>
		</ul>
	</li>
	<li>Таблицу трассировки для одной (на свой выбор) из пар значений X и Y</li>
</ol>

<h3>При составлении программы и отчёта необходимо учитывать, что:</h3>
<ol>
	<li>Исходные данные и результат являются знаковыми числами</li>
	<li>Исходное данное X является неотрицательным</li>
</ol>

<% } else { %>

<div class='portlet-msg-error'>Необходимо войти под студенческим пользователем</div>

<% } %>
