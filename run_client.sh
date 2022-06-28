#!/bin/bash

echo "expect 6..."
curl -X GET "http://localhost:5151/waro?prize_card=10&max_card=12&mode=max&cards=4&cards=6&cards=2"
echo ""

echo "expect 2..."
curl -X GET "http://localhost:5151/waro?prize_card=10&max_card=12&mode=min&cards=4&cards=6&cards=2"
echo ""

echo "expect 11..."
curl -X GET "http://localhost:5151/waro?prize_card=10&max_card=12&mode=nearest&cards=4&cards=12&cards=11"
echo ""

echo "expect 4..."
curl -X GET "http://localhost:5151/waro?prize_card=10&max_card=12&mode=next&cards=4&cards=6&cards=2"
echo ""

