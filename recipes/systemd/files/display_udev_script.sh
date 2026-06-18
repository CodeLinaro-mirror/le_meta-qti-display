#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

DUMP_TO_KMSG=/dev/kmsg
    case "$1" in
    mdss_power)
        # Script to set group ownership and permissions for MDSS MDP power control node
        chown -h root.graphics /sys/devices/platform/soc/ae00000.qcom,mdss_mdp/power/control
        chmod 0664 /sys/devices/platform/soc/ae00000.qcom,mdss_mdp/power/control
    ;;
    *)
        echo " qcmap: Invalid option. option: $1 " > $DUMP_TO_KMSG
    ;;

    esac
exit 0

