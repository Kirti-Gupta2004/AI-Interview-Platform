import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-past-questions',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './past-questions.html',
  styleUrls: ['./past-questions.css']
})
export class PastQuestionsComponent implements OnInit {
  historyList: any[] = [];

  ngOnInit() {
    // Page load hote hi browser ke localStorage se purani history nikalna
    const savedData = localStorage.getItem('pastQuestions');
    if (savedData) {
      this.historyList = JSON.parse(savedData);
    }
  }

  // History ko poora khali karne ke liye function
  clearHistory() {
    if (confirm("Kya aap saari practice history delete karna chahte hain?")) {
      localStorage.removeItem('pastQuestions');
      this.historyList = [];
    }
  }
}