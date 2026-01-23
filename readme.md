# Admin Extensions Feature Flag for XWiki

## Overview

This project provides a feature flag to hide/show the Extensions section in XWiki Global Administration.

## Features

- **Feature Flag**: Enable/disable Extensions section via configuration
- **Security**: Blocks both UI access and direct URL access
- **Flexible Configuration**: Configure via xwiki.properties, environment variables, or UI
- **Zero Overhead**: Minimal performance impact
- **Test Coverage**: Comprehensive unit and integration tests

## Installation

### Prerequisites
- XWiki 15.0+
- Java 11+
- Maven 3.6+

### Quick Installation

1. **Download the distribution:**
```bash
wget https://github.com/xwiki-contrib/featureflag-adminextensions/releases/download/v1.0.0/featureflag-adminextensions-1.0.0.zip