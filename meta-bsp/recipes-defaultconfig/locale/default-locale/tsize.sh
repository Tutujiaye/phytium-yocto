#!/bin/bash
#
# SPDX-License-Identifier: BSD-3-Clause
#
# This script queries terminal for current window size and does a stty
# to set rows and columns to that size.  It is useful for terminals that
# are confused about size.  This often happens when accessing consoles
# via a serial port.
if [ -t 0 -a $# -eq 0 ]; then
	if [ ! -x @BINDIR@/resize ] ; then
		if [ -n "$BASH_VERSION" ] ; then
# Optimized resize funciton for bash
resize() {
	local x y
	IFS='[;' read -t 2 -p $(printf '\e7\e[r\e[999;999H\e[6n\e8') -sd R _ y x _
	[ -n "$y" ] && \
	echo -e "COLUMNS=$x;\nLINES=$y;\nexport COLUMNS LINES;" && \
	stty cols $x rows $y
}
		else
# Portable resize function for ash/bash/dash/ksh
# with subshell to avoid local variables
resize() {
	(o=$(stty -g)
	stty -echo raw min 0 time 2
	printf '\0337\033[r\033[999;999H\033[6n\0338'
	if echo R | read -d R x 2> /dev/null; then
		IFS='[;R' read -t 2 -d R -r z y x _
	else
		IFS='[;R' read -r _ y x _
	fi
	stty "$o"
	[ -z "$y" ] && y=${z##*[}&&x=${y##*;}&&y=${y%%;*}
	[ -n "$y" ] && \
	echo "COLUMNS=$x;"&&echo "LINES=$y;"&&echo "export COLUMNS LINES;"&& \
	stty cols $x rows $y)
}
		fi
	fi
	# Use the EDITOR not being set as a trigger to call resize
	# and only do this for /dev/tty[A-z] which are typically
	# serial ports
	if [ -z "$EDITOR" -a "$SHLVL" = 1 ] ; then
		case $(tty 2>/dev/null) in
			/dev/tty[A-z]*) resize >/dev/null;;
		esac
	fi
fi

EDITOR="vi"
