#!/usr/bin/env bash
set -euo pipefail

# Usage: android-start-debug.sh <package> <activity> [port]
PKG=${1:-}
ACT=${2:-}
PORT=${3:-8700}

if [[ -z "$PKG" || -z "$ACT" ]]; then
  echo "Usage: $0 <package> <activity> [port]" >&2
  exit 1
fi

echo "Starting $PKG/$ACT with debugger (-D) and preparing JDWP forward on tcp:$PORT ..."

# Start the activity in debug mode (-D makes it wait for debugger)
adb shell am start -D -n "$PKG/$ACT" >/dev/null || true

# Wait for process pid
PID=""
for i in {1..40}; do
  PID=$(adb shell pidof "$PKG" | tr -d '\r' | tr -d '\n' || true)
  if [[ -n "$PID" ]]; then
    break
  fi
  sleep 0.5
done

if [[ -z "$PID" ]]; then
  echo "Failed to find PID for $PKG" >&2
  exit 2
fi

# Wait for JDWP to be available for that pid
for i in {1..40}; do
  if adb jdwp | tr -d '\r' | grep -qw "$PID"; then
    break
  fi
  sleep 0.5
done

echo "Forwarding tcp:$PORT -> jdwp:$PID"
adb forward "tcp:$PORT" "jdwp:$PID" >/dev/null
echo "JDWP forward ready on localhost:$PORT"
