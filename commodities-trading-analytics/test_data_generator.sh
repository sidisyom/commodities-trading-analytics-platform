#!/bin/bash

TRADES_ENDPOINT=http://localhost:8080/trades
INSIGHTS_ENDPOINT=http://localhost:8080/insights
PAYLOAD=$(cat <<EOF
        [
          {
          "commodity": "GOLD",
          "traderId": "T123",
          "price": 2023.5,
          "quantity": 10,
          "timestamp": "2025-04-10T14:30:00Z"
          },
          {
          "commodity": "OIL",
          "traderId": "T123",
          "price": 100.302,
          "quantity": 10,
          "timestamp": "2025-04-10T14:30:00Z"
          },
          {
          "commodity": "OIL",
          "traderId": "T456",
          "price": 85.2,
          "quantity": 5,
          "timestamp": "2025-04-10T15:10:00Z"
          },
          {
          "commodity": "GOLD",
          "traderId": "T456",
          "price": 1998.121,
          "quantity": 8,
          "timestamp": "2025-04-10T14:30:00Z"
          }
        ]
EOF
)

echo "POSTing: $PAYLOAD to $TRADES_ENDPOINT"
response=$(curl -X POST $TRADES_ENDPOINT -H "Content-Type: application/json" -d "$PAYLOAD")
echo "Response: $response"

echo "GET-ing trades"
response=$(curl -X GET $TRADES_ENDPOINT)
echo "Response: $response"

echo "GET-ing insights"
response=$(curl -X GET $INSIGHTS_ENDPOINT)
echo "Response: $response"