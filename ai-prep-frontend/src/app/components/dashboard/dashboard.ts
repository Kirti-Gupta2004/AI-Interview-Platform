import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent {
  selectedFile: File | null = null;
  experienceLevel: string = 'Fresher';
  questionsList: string[] = [];
  isLoading: boolean = false;

  constructor(private apiService: ApiService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // 🔥 YAHAN PAR AAPKA POORA METHOD AYEGA 🔥
  onGenerate() {
    if (!this.selectedFile) {
      alert('Please upload a resume first!');
      return;
    }

    this.isLoading = true;
    this.questionsList = []; 
    
    const mockResumeText = "Java Developer skilled in Core Java, Spring Boot, SQL, Angular."; 
    
    this.apiService.generateQuestions(mockResumeText, this.experienceLevel).subscribe({
      next: (res: any) => {
        this.isLoading = false;
        
        if (res && res.candidates && res.candidates[0]?.content?.parts?.[0]?.text) {
          const textResponse = res.candidates[0].content.parts[0].text;
          this.questionsList = textResponse.split('\n').filter((line: string) => line.trim() !== '');

          // 🔥 PAST QUESTIONS STORAGE LOGIC 🔥
          const currentHistory = JSON.parse(localStorage.getItem('pastQuestions') || '[]');
          
          const newItem = {
            date: new Date().toLocaleDateString(),
            time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
            profile: this.experienceLevel,
            questions: this.questionsList
          };
          
          currentHistory.unshift(newItem);
          localStorage.setItem('pastQuestions', JSON.stringify(currentHistory));

        } else {
          alert('Response format unexpected.');
        }
      },
      error: (err: any) => {
        this.isLoading = false;
        alert('API Error. Check console.');
      }
    });
  }
}