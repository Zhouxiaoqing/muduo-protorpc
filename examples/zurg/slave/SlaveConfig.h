#ifndef MUDUO_PROTORPC_ZURG_SLAVECONFIG_H
#define MUDUO_PROTORPC_ZURG_SLAVECONFIG_H

#include <muduo/base/Types.h>

namespace zurg
{
using muduo::string;

struct SlaveConfig
{
  SlaveConfig()
    : succeed_(false),
      masterAddress_(),
      listenPort_(-1),
      zombieInterval_(10)
  {
  }

  SlaveConfig(const char* masterAddress, int listenPort)
    : succeed_(true),
      masterAddress_(masterAddress),
      listenPort_(listenPort),
      zombieInterval_(10)
  {
  }

  bool succeed_;
  string masterAddress_;
  int listenPort_;
  int zombieInterval_;
};

}

#endif  // MUDUO_PROTORPC_ZURG_SLAVECONFIG_H
