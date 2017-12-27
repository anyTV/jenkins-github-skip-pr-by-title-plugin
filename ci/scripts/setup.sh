#!/usr/bin/env bash
cwd=$(pwd)

function setup_mvn()
{
  cd ${cwd}

  if [ -d node_modules ]; then
    rm -rf target
  fi
}

setup_mvn
