import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; // 🔥 Environment file ko import kiya

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  // 🔥 Ab key directly code mein nahi dikhegi, environment se aayegi
  private apiKey = environment.geminiApiKey; 
  private apiUrl = 'https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent';

  constructor(private http: HttpClient) { }

  generateQuestions(resumeText: string, expLevel: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const params = new HttpParams().set('key', this.apiKey);

    const body = {
      contents: [{
        parts: [{ 
          text: `Act as a technical interviewer. Based on this resume profile context: ${resumeText}, generate 5 distinct technical interview questions for a ${expLevel} level candidate.` 
        }]
      }]
    };

    return this.http.post(this.apiUrl, body, { headers, params });
  }
}