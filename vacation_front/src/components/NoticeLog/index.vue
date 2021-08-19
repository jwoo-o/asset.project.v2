<template>
  <div>
    <el-badge :is-dot="true" style="line-height: 25px;margin-top: -10px;" @click.native="dialogTableVisible=true">
      <svg-icon :icon-class="'bell'" class-name="bell-avatar"/>
    </el-badge>

    <el-dialog :visible.sync="dialogTableVisible" width="80%" append-to-body>
      <div slot="title">
        <span style="padding-right: 10px;">Error Log</span>
        <el-button size="mini" type="primary" icon="el-icon-delete" @click="clearAll">Clear All</el-button>
      </div>
      <el-table :data="noticeLog" border>
        <el-table-column label="Message">
          <template slot-scope="{row}">
            <div>
              <span class="message-title">Msg:</span>
              <el-tag type="danger">
                {{ row.err.message }}
              </el-tag>
            </div>
            <br>
            <div>
              <span class="message-title" style="padding-right: 10px;">Info: </span>
              <el-tag type="warning">
                {{ row.vm.$vnode.tag }} error in {{ row.info }}
              </el-tag>
            </div>
            <br>
            <div>
              <span class="message-title" style="padding-right: 16px;">Url: </span>
              <el-tag type="success">
                {{ row.url }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Stack">
          <template slot-scope="scope">
            {{ scope.row.err.stack }}
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'NoticeLog',
  data() {
    return {
      dialogTableVisible: false,
      isFullscreen: false
    }
  },
  computed: {
    noticeLog() {
      return []
    }
  },
  methods: {
    clearAll() {
      this.dialogTableVisible = false
      this.$store.dispatch('errorLog/clearErrorLog')
    }
  }
}
</script>

<style scoped>
    .message-title {
        font-size: 16px;
        color: #333;
        font-weight: bold;
        padding-right: 8px;
    }
    .bell-avatar {
    cursor: pointer;
    width: 30px;
    height: 30px;
    border-radius: 10px;
  }
</style>
© 2021 GitHub, Inc.
