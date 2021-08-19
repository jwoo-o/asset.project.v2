<template>
  <div v-if="width<1200" class="content" style="margin-bottom: 20px">
    <treeselects
      v-model="code"
      :options="$store.getters.orgList"
      placeholder="선택"
      :default-expand-level="1"
      style="font-size: small"
      @select="handleNodeClick"
    />
  </div>
  <el-card v-else :body-style="{padding : '0px'}" style="height: 850px;margin-bottom: 10px;" shadow="never" class="grid-wrapper org-tree">
    <div class="header">
      <span>조직도</span>
    </div>
    <div class="content">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :lg="24">
          <el-input v-model="filterText" clearable placeholder="조직명" size="small" />
        </el-col>
      </el-row>
    </div>
    <div style="padding: 10px">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :lg="24">
          <el-tree
            ref="tree"
            :data="$store.getters.orgList"
            :expand-on-click-node="false"
            node-key="orgCode"
            :default-expanded-keys="['00000000']"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
          />
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
import Treeselects from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { mapGetters } from 'vuex'

export default {
  name: 'OrgTree',
  components: { Treeselects },
  // eslint-disable-next-line vue/require-prop-types
  props: {
    orgCode: {
      type: String,
      default: undefined
    }
  },
  data() {
    return {
      filterText: '',
      code: this.orgCode
    }
  },
  computed: {
    ...mapGetters([
      'width'
    ])
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    filterNode(value, data) {
      if (!value) {
        return true
      }
      return data.label.indexOf(value) !== -1
    },
    handleNodeClick(data) {
      this.$emit('orgTree', data)
    }
    /* renderContent(h, { node, data, store }) {
      return (
        <span class='custom-tree-node' style='font-size:14px;'>
          <svg-icon icon-class='peoples' style='margin-right:5px;'/>
          {node.label}
        </span>)
    }*/
  }
}
</script>

<style scoped>
  .clss {
    font-size: medium;
  }
</style>
<style>
  .org-tree {
    overflow: auto;
  }

  .org-tree::-webkit-scrollbar {
    width: 10px;
  }

  .org-tree::-webkit-scrollbar-thumb {
    background-color: #626ea6;
    border-radius: 10px;
    background-clip: padding-box;
    border: 3px solid transparent;
  }

  .org-tree::-webkit-scrollbar-track {
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: inset 0px 0px 5px white;
  }
</style>
