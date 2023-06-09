# Exchange Rate Calculator App

## Overview

The Exchange Rate Calculator App is a simple Android application that allows users to convert currencies from Euro to Czech Koruna (CZK) based on the latest exchange rates obtained from an external API.

## Installation and Configuration

To install and run the app, follow these steps:

1. Clone the project from the Github repository.
2. Open the project in Android Studio.
3. In the ExchangeRatesApi.kt file, replace the BASE_URL variable with the base URL of the API you wish to use.
4. Run the app on an emulator or physical device.

Note: Some APIs require an API key to be passed in the URL. If your API requires an API key, add it to the URL in the BASE_URL variable in the ExchangeRatesApi.kt file.

## Usage

When you open the app, you will see two fields: one for entering the amount in Euros and one for displaying the converted amount in Czech Koruna. The exchange rate is automatically fetched from the external API when the app is launched. To convert a value, simply enter an amount in Euros and click the "Convert" button. The converted amount in Czech Koruna will be displayed in the second field.

## Dependencies

The app uses the following dependencies:

Retrofit: For making network requests to the external API.
Gson: For parsing JSON responses from the API.
ViewModel and LiveData: For managing the UI state and data in a lifecycle-aware manner.

## API Used

The app uses the https://exchangerate.host/#/ to fetch the latest exchange rates.
