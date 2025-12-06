#!/bin/bash

# TravelLens - Start Backend Server

echo "🚀 Starting TravelLens Backend..."
echo "Backend will run on http://localhost:8080"
echo ""

cd backend
nix-shell -p maven jdk17 --run "mvn spring-boot:run"
