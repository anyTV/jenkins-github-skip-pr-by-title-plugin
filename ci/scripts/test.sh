#!/usr/bin/env bash
cwd=$(pwd)

function test_mvn()
{
  cd ${cwd}

  mvn test
}

test_mvn
