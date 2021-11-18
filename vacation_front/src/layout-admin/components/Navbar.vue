<template>
  <div class="navbar">
    <el-col :xs="22" :sm="19" :lg="20">
      <el-menu
        :default-active="getIndex"
        mode="horizontal"
        background-color="#527198"
        text-color="#fff"
        active-text-color="#9CC3D7"
        @select="handleSelect"
      >
        <el-menu-item v-if="width > 760" style="width: 150px;text-shadow: 0 0 0 black; color: transparent; text-align: center;">
          <img src="@/assets/joeun.svg">
        </el-menu-item>
        <el-submenu index="admin-vacation-list">
          <template slot="title">
            <el-badge :value="$store.getters.alarmCount !== 0 ? 'new': ''" style="line-height: 35px;margin-top: -1px;" >
              인사관리
            </el-badge>
          </template>
          <el-menu-item index="admin-vacation-list">
            휴가 현황
          </el-menu-item>
          <el-menu-item index="organization-index">
            부서 관리
          </el-menu-item>
          <el-menu-item index="user-list">
            사원 관리
          </el-menu-item>
          <el-menu-item index="user-cert">
            <el-badge :value="$store.getters.alarmCount === 0 ? '' : $store.getters.alarmCount" >
              증명서 관리
            </el-badge>
          </el-menu-item>
        </el-submenu>
        <el-submenu index="admin-asset-dashboard">
          <template slot="title">자산관리</template>
          <el-menu-item index="admin-asset-dashboard">
            자산 현황
          </el-menu-item>
          <el-menu-item index="admin-asset-list">
            자산 정보
          </el-menu-item>
        </el-submenu>
        <el-submenu index="admin-approve-line">
          <template slot="title">환경설정</template>
          <el-menu-item index="admin-approve-line">
            결재 라인
          </el-menu-item>
          <el-menu-item index="common">
            공통 코드
          </el-menu-item>
        </el-submenu>
        <el-menu-item index="board-notice">
          공지 사항
        </el-menu-item>
        <el-menu-item index="admin-seat-index">
          배치도
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col v-if="width > 760" :sm="3" :lg="3">
      <div style="margin-top: 18px" align="right">
        <span class="header_span">{{ $store.getters.rankNm }} {{ $store.getters.userName }}</span>
      </div>
    </el-col>
    <el-col :xs="2" :sm="2" :lg="1">
      <div class="right-menu">
        <!--<NoticeLog class="right-menu-item hover-effect" />-->
        <el-dropdown class="avatar-container right-menu-item hover-effect">
          <div class="avatar-wrapper">
            <svg-icon icon-class="main_user" class-name="user-avatar" style="background-color: white" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="logout">
              <span style="display:block;">로그아웃</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-col>

  </div>
</template>

<script>

import { mapGetters } from 'vuex'

export default {
  // eslint-disable-next-line vue/no-unused-components
  // eslint-disable-next-line vue/require-prop-types
  data() {
    return {
      active: '1'
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ]),
    getIndex() {
      if (!this.$route.meta.hidden) {
        return this.$route.name
      } else {
        return this.$route.meta.parent
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    handleSelect(index) {
      if (index === null) {
        index = 'admin-vacation-list'
      }
      this.$router.push({ name: index })
    },
    async logout() {
      await this.$store.dispatch('admin/logout')
      this.$router.push('/mng')
    }
  }
}
</script>

<style lang="scss" scoped>
    .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
    }

    .el-icon-arrow-down {
        font-size: 12px;
    }

    .header_span {
        display: block;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        font-size: 16px;
        color: #fff;
    }

    .navbar {
        height: 60px;
        overflow: hidden;
        position: relative;
        background: #527198;
        box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

        .right-menu {
            float: right;
            height: 100%;
            line-height: 50px;
            margin-top: 4px;

            &:focus {
                outline: none;
            }

            .right-menu-item {
                display: inline-block;
                padding: 0 8px;
                height: 100%;
                font-size: 18px;
                color: #5a5e66;
                vertical-align: text-bottom;

                &.hover-effect {
                    cursor: pointer;
                    transition: background .3s;

                    &:hover {
                        background: rgba(0, 0, 0, .025)
                    }
                }
            }

            .avatar-container {
                margin-right: 10px;

                .avatar-wrapper {
                    margin-top: 5px;
                    position: relative;

                    .user-avatar {
                        cursor: pointer;
                        width: 30px;
                        height: 30px;
                        border-radius: 10px;
                    }

                    .el-icon-caret-bottom {
                        cursor: pointer;
                        position: absolute;
                        right: -20px;
                        top: 25px;
                        font-size: 12px;
                    }
                }
            }
        }
    }
</style>
