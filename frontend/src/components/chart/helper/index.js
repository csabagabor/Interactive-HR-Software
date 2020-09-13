import axios from 'axios';

export default {


    async getChartData(url) {
        let resp = await axios.get(url);

        let colors = [
            'rgba(255, 50, 100, 0.5)',
            'rgba(120, 50, 100, 0.5)',
            'rgba(120, 150, 100, 0.5)',
            'rgba(30,74,120,0.5)',
            'rgba(25,120,11,0.5)',
            'rgba(120,10,31,0.5)',
            'rgba(120,115,4,0.5)',
            'rgba(120,1,8,0.5)',
            'rgba(11,9,120,0.5)',
        ]

        let map = resp.data
        let colorIndex = 0

        let data = []
        let borderColors = []
        let labels = []

        for (let obj in map) {
            labels.push(obj)
            data.push(map[obj])
            borderColors.push(colors[colorIndex])

            colorIndex++
            if (colorIndex >= colors.length) {
                colorIndex = 0
            }

        }
        return {
            data: data,
            labels: labels,
            borderColors: borderColors,
            backgroundColors: borderColors.map(color => color.replace(/[^,]+(?=\))/, '0.2')),//replaces alpha value in the color
        }
    }
}