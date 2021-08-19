<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from '@/components/Mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    chartData: {
      type: Array,
      required: true
    },
    currentVersion: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions(chartData) {
      const key = []
      chartData.map((value) => {
        key.push(value.name)
        /*if (value.name === this.currentVersion) {
          value.itemStyle = {
            color: '#7b81d8'
          }
        }*/
      })
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{b} : {c}'
        },
        xAxis: {
          type: 'category',
          data: key
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: chartData,
          type: 'bar'
        }]
      })
    }
  }
}
</script>
