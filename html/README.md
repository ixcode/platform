# Platform - HTML

## What is it?

A library that allows you to produce HTML content. The core model is one of componentisation. This means I wish to have a page rendered which is composed of various components. These components may be rendered on the server, or be included client side. They will require a datasource and may need to update a server side reference.

Components will need to have templates which can easily be edited to change look and feel. You may have several components which share the same datasource - for example a login component for the home page and one for an article page.

Components will need to be styled in accordance with an overall theme and yet have component specific style attributes.

Components may contain sub-components?

Components may interact - i.e. the state of one component may be influenced by the state of another component (e.g. the "related articles component" might be interested in what article the "article display" component has on it.

All components should have a "degradation mode" - no data, cannot render in a particular browser, different browser versions

Components may consist of javascript.

Pages are the aggregator (classic composite pattern) the HTMl platform knows nothing about how you got to the page, so nothing is assumed about url routing.

## Technical Architecture

Define a component template:

login-lightweight.html

Define a page:

home-page.html
<!-- $$(extends basic-page)$$ -->

<head>
 <link />
</head>

<body>
</body>





