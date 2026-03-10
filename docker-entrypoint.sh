#!/bin/bash
set -e

# Default APP_PROFILE to prod if not set
export APP_PROFILE=${APP_PROFILE:-prod}

echo "Starting ecommerce app [profile=${APP_PROFILE}]..."

exec /usr/bin/supervisord -c /etc/supervisor/conf.d/supervisord.conf
