<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :before-close="handleClose"
    :width="width < 768 ? '95%' : width < 1200 ? '70%' : '30%'"
  >
    <el-form ref="certForm" :model="certForm" :rules="certRules">
      <el-form-item label-width="100px" label="종류" size="small" prop="type">
        <el-select v-model="certForm.type" @change="getUserList">
          <el-option value="CAREER" label="경력증명서" />
          <el-option value="EMP" label="재직증명서" />
        </el-select>
      </el-form-item>
      <el-form-item label-width="100px" label="이름" size="small" prop="type">
        <treeselects
          v-model="certForm.userId"
          :disabled="certForm.type === ''"
          :options="userList"
          :multiple="true"
          placeholder="선택"
          :default-expand-level="1"
          style="font-size: small"
          :normalizer="normalizer"
        />
      </el-form-item>
      <el-form-item label-width="100px" label="용도" size="small" prop="purpose">
        <el-input v-model="certForm.purpose" />
      </el-form-item>
      <el-form-item label-width="100px" label="제출처" size="small" prop="submit">
        <el-input v-model="certForm.submit" />
      </el-form-item>
      <el-form-item label-width="100px" label="직인" size="small" prop="seal">
        <el-checkbox v-model="certForm.seal" />
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button size="small" @click="handleClose">취소</el-button>
      <el-button size="small" type="primary" @click="handleApprove">발급</el-button>
    </span>
  </el-dialog>
</template>

<script>
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { mapGetters } from 'vuex'
import { necessary_message } from '../../message/constant'

export default {
  name: 'CertificateDialog',
  components: { Treeselects },
  props: {
    certForm: {
      type: Object,
      require: true,
      default: function() {
        return {
          purpose: '',
          submit: '',
          seal: true,
          userId: []
        }
      }
    },
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateCommon = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error(necessary_message))
      } else {
        callback()
      }
    }
    return {
      certRules: {
        userId: [{ required: true, trigger: 'change', validator: validateCommon }],
        type: [{ required: true, trigger: 'change', validator: validateCommon }],
        purpose: [{ required: true, trigger: 'blur', validator: validateCommon }],
        submit: [{ required: true, trigger: 'blur', validator: validateCommon }]
      },
      userList: [],
      normalizer(node) {
        return {
          id: node.userId,
          label: node.orgName + ' - ' + node.name + ' ' + node.rankNm,
          children: ''
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  methods: {
    handleClose() {
      this.$emit('handleClose')
    },
    handleApprove() {
      this.$refs.certForm.validate(async valid => {
        if (valid) {
          this.$emit('handleApprove')
        } else {
          return false
        }
      })
    },
    getUserList() {
      this.userList = this.$store.getters.userList.filter(item => this.certForm.type === 'CAREER' ? true : item.useYn)
    }
  }
}
</script>

<style scoped>

</style>
