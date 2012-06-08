#!/bin/bash
# Type chmod +x xec.sh in this folder to make it executable
# add this to your ~/.bash_profile to make it simpler to call:
# export PATH=.:$PATH

CURRENT_DIR=$(pwd)
PROJECT_NAME=$(basename "$PWD")
ANT_PATH=$(which ant)

function calculate.ibxScriptFileName() {
    local RAW_IBX_SCRIPT_FILE=$0
    local LINKED_IBX_SCRIPT_FILE=$(readlink $RAW_IBX_SCRIPT_FILE)
    IBX_SCRIPT_FILE=$LINKED_IBX_SCRIPT_FILE
    if [ -z "$LINKED_IBX_SCRIPT_FILE" ]; then
        IBX_SCRIPT_FILE=$RAW_IBX_SCRIPT_FILE
    fi
    export IBX_HOME=$(dirname $IBX_SCRIPT_FILE)
}

function init() {
    calculate.ibxScriptFileName
}

function report.config() {
	echo "-----------------------------------------------------------------------------"
	echo "Ibex Build System"
	echo "-----------------------------------------------------------------------------"
	echo "Project name   : [$PROJECT_NAME]"
	echo "Working in     : $CURRENT_DIR"
	echo "Home           : $IBX_HOME"
	echo "Version        : alpha-01"
	echo "Args           : $*"
	echo "-----------------------------------------------------------------------------"
}

function report.completed() {
	#type man strftime to see full list of date formatting options.
	CURRENT_DATE=$(date "+%a %d %b %Y")
	CURRENT_TIME=$(date "+%H:%M:%S")
	echo "-----------------------------------------------------------------------------"
	echo "Complete at $CURRENT_TIME on $CURRENT_DATE."
	echo "-----------------------------------------------------------------------------"
	echo
}

function command.help() {
	echo "Usage: ibx <command> args"
	echo .
	cat "$IBX_HOME/ibex.txt"
	echo .
	echo "install  : Installs the Ibex into your system"
	echo "build    : Runs an intelligent build over your sources."

}


function command.build() {
   java -cp "$IBX_HOME/lib/*" ixcode.platform.build.ModuleBuilder "$CURRENT_DIR" "$@"
}

function command.install() {
    echo "Ok, I'm going to install myself in your path."
    rm "/usr/local/bin/ibx"
    cd "$IBX_HOME"
    ln -s "$PWD/ibx.sh" "/usr/local/bin/ibx"
    cd -
}


function executeCommandFunction() {
    local COMMAND=$1
    local COMMAND_FUNCTION=$2
    shift
    shift
    local ARGUMENTS=$*
	FUNCTION_EXISTS=$(type "$COMMAND_FUNCTION" 2> /dev/null | grep "is a function")
	if [ -z "$FUNCTION_EXISTS" ] ; then
		echo "Unknown command [$COMMAND]"
		command.help
		exit -1
	fi
	eval $COMMAND_FUNCTION $ARGUMENTS
}

function processCommand() {
	local COMMAND=$1
	shift
	local ARGUMENTS=$*

	echo "Executing command [$COMMAND] with arguments [$ARGUMENTS]"

	executeCommandFunction $COMMAND "command.$COMMAND" $ARGUMENTS
}


init
report.config $*
processCommand $*
report.completed




