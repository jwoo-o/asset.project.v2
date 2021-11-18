<template>
  <el-dialog
    :visible.sync="dialogVisible"
    width="400px"
    :before-close="handleClose"
  >
    <div ref="qrCodeInfo" style="width: 370px;height: 120px;padding: 2px 0 2px 25px">
      <el-row>
        <el-col :span="14">
          <img src="@/assets/joeun_qr_image.png" style="height: 32px">
          <table border="1px" style="border-collapse:collapse;" width="210">
            <tr>
              <td style="padding: 10px">
                <b>자산번호</b> : {{ info.assetId }}
              </td>
            </tr>
            <tr>
              <td style="padding: 10px">
                <b>취득일자</b> : {{ info.purchaseDate }}
              </td>
            </tr>
          </table>
        </el-col>
        <el-col :span="10" align="center">
          <vue-qrcode :value="data" :margin="4" :width="120" />
        </el-col>
      </el-row>
    </div>
    <div style="text-align: right">
      <el-button type="primary" icon="el-icon-download" @click="handleDownload">다운로드</el-button>
    </div>
  </el-dialog>
</template>

<script>
import VueQrcode from 'vue-qrcode'
import html2canvas from 'html2canvas'

export default {
  name: 'QrCodeCreate',
  components: {
    VueQrcode
  },
  // eslint-disable-next-line vue/require-prop-types
  props: ['data', 'info', 'dialogVisible'],
  methods: {
    handleClose() {
      this.$emit('handleClose')
    },
    async handleDownload() {
      const assetId = this.info.assetId
      await html2canvas(this.$refs.qrCodeInfo).then(function(canvas) {
        const link = document.createElement('a')
        link.href = canvas.toDataURL('image/jpeg').replace('image/jpeg', 'image/octet-stream')
        link.download = `${assetId}.jpg`
        document.body.appendChild(link)
        link.click()
      })
    }
  }
}
</script>

<style scoped>

</style>
