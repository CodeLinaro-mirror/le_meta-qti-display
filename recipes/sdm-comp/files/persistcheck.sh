#!/bin/sh
# Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

timeout=30

while [ ! -d "/persist/display" ]
do
  if [ "$timeout" == 0 ]; then
    echo "ERROR: Timeout while waiting for creation of /persist/display"
    exit 1
  fi

  sleep 3

  ((timeout--))
done

if [ -d "/persist/display" ]; then
    echo "/persist/display is available"
else
    echo "/persist/display is not available"
fi
